package org.vlang.ide.inspections.general

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementVisitor
import org.vlang.ide.codeInsight.VlangAttributesUtil
import org.vlang.ide.inspections.VlangBaseInspection
import org.vlang.lang.psi.*
import org.vlang.lang.psi.VlangFile.Companion.normalizeSlashes
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.unwrapAlias
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.unwrapPointer
import org.vlang.lang.psi.types.VlangStructTypeEx
import org.vlang.lang.psi.types.VlangUnknownTypeEx

class VlangCallArgumentsCountMismatchInspection : VlangBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitCallExpr(call: VlangCallExpr) {
                val containingFile = call.containingFile
                val virtualFile = containingFile?.virtualFile ?: return
                val path = virtualFile.path.normalizeSlashes()
                // TODO: remove after resolving improvements
                if (path.contains("/examples/") || path.contains("/tests/") || path.contains("tutorials")) return

                val project = call.project
                if (project.service<DumbService>().isDumb) return

                val identifier = call.identifier ?: return
                val called = call.resolve() as? VlangSignatureOwner ?: return
                if (called is VlangNamedElement) {
                    val fqn = called.getQualifiedName() ?: return
                    // skip some magic functions
                    if (fqn == "json.decode" || fqn == "builtin.array.sort" || fqn == "builtin.new_node" || fqn == "builtin.new_map" || fqn == "main.App.text") {
                        return
                    }

                    val name = called.name ?: return
                    // skip C.* and JS.* functions
                    if (name.startsWith("C.") || name.startsWith("JS.")) {
                        return
                    }
                }

                val signature = called.getSignature() ?: return
                val expectedParameters = signature.parameters.paramDefinitionList

                // when the last parameter is [param] struct
                // this parameter can be omitted
                val lastIsParamsStruct = lastIsParamsStruct(call.project, expectedParameters)

                // fn foo(a int, b int, c ...int)
                // when the last parameter is a variadic parameter,
                // we don't care about more arguments than parameters
                val withVariadic = expectedParameters.size > 0 && expectedParameters.last().isVariadic

                val actualArguments = call.argumentList.elementList

                // when foo(1, ...arr)
                val withUnpacking = lastIsUnpacking(actualArguments)
                if (withUnpacking) {
                    if (withVariadic) {
                        // don't check this
                        return
                    }

                    val countArgumentsBeforeUnpacking = actualArguments.size - 1
                    if (countArgumentsBeforeUnpacking > expectedParameters.size) {
                        holder.registerProblem(
                            identifier,
                            "Too many arguments in call to '${identifier.text}', expected at most ${expectedParameters.size} arguments, got $countArgumentsBeforeUnpacking",
                            ProblemHighlightType.GENERIC_ERROR
                        )
                    }
                    return
                }

                // foo(1, 2, name: 'John')
                val withTrailingStruct = actualArguments.any { it.key != null }

                // from above is 1, 2
                val actualPlainArguments = actualArguments.takeWhile { it.key == null }

                if (withVariadic) {
                    // fn foo(a int, b int, c ...int)
                    // foo(1, 2, 3)
                    // minExpectedCountArguments => 2, since variadic parameter can be omitted
                    val minExpectedCountArguments = expectedParameters.size - 1
                    if (actualPlainArguments.size < minExpectedCountArguments) {
                        holder.registerProblem(
                            identifier,
                            "Not enough arguments in call to '${identifier.text}', expected at least $minExpectedCountArguments arguments, got ${actualPlainArguments.size}",
                            ProblemHighlightType.GENERIC_ERROR
                        )
                    }
                    return
                }

                // [param] struct can be omitted, so we don't care about it
                val minExpectedCountArguments = expectedParameters.size - if (withTrailingStruct || lastIsParamsStruct) 1 else 0

                // when function called with trailing struct
                // expected count arguments is less by 1
                // same for [param] struct
                val expectedCountArguments = expectedParameters.size
                val actualCountArguments = actualPlainArguments.size

                if (actualCountArguments < minExpectedCountArguments) {
                    val message = if (withTrailingStruct) {
                        "Not enough arguments in call to '${identifier.text}', expected at least $minExpectedCountArguments arguments before trailing struct params, but got $actualCountArguments"
                    } else {
                        "Not enough arguments in call to '${identifier.text}', expected at least $minExpectedCountArguments arguments, but got $actualCountArguments"
                    }

                    holder.registerProblem(
                        identifier,
                        message,
                        ProblemHighlightType.GENERIC_ERROR
                    )
                    return
                }

                if (expectedCountArguments == actualCountArguments) {
                    if (actualCountArguments > 0 && withTrailingStruct) {
                        holder.registerProblem(
                            identifier,
                            "Too many arguments in call to '${identifier.text}', unexpected trailing struct params after last argument",
                            ProblemHighlightType.GENERIC_ERROR
                        )
                    }
                    return
                }

                if (actualCountArguments > expectedCountArguments) {
                    val message = if (withTrailingStruct) {
                        "Too many arguments in call to '${identifier.text}', expected $expectedCountArguments arguments before trailing struct params, but got $actualCountArguments"
                    } else {
                        "Too many arguments in call to '${identifier.text}', expected $expectedCountArguments arguments, but got $actualCountArguments"
                    }

                    holder.registerProblem(
                        identifier,
                        message,
                        ProblemHighlightType.GENERIC_ERROR
                    )
                }
            }

            private fun lastIsParamsStruct(project: Project, expectedParameters: List<VlangParamDefinition>): Boolean {
                val lastParameter = expectedParameters.lastOrNull() ?: return false
                val lastType = lastParameter.getType(null)?.unwrapPointer()?.unwrapAlias() ?: return false
                // temp workaround
                // for some reason getType() returns VlangUnknownTypeEx at start of project
                if (lastType is VlangUnknownTypeEx) return true
                if (lastType !is VlangStructTypeEx) return false
                val struct = lastType.resolve(project) ?: return false
                return VlangAttributesUtil.isParamsStruct(struct)
            }

            private fun lastIsUnpacking(arguments: List<VlangElement>): Boolean {
                if (arguments.isEmpty()) return false
                val lastArgument = arguments.last().value ?: return false
                val lastExpression = lastArgument.expression
                return lastExpression is VlangUnpackingExpression
            }
        }
    }
}
