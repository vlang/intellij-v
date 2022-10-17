package org.vlang.lang.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.openapi.editor.Document
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiDocumentManager
import com.intellij.util.ProcessingContext
import org.vlang.ide.codeInsight.VlangTypeInferenceUtil
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangLangUtil
import org.vlang.lang.psi.types.*
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.utils.parentNth

class VlangClosureCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            psiElement().withElementType(VlangTypes.IDENTIFIER),
            ClosureNameCompletionProvider()
        )
    }

    private class ClosureNameCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val pos = parameters.position

            // don't use parentOfType because it will return call expr even in body of closure
            val callExpr = pos.parentNth<VlangCallExpr>(5) ?: return
            val index = VlangLangUtil.indexInCall(pos) ?: return

            val refExpr = callExpr.expression as? VlangReferenceExpression ?: return
            val function = refExpr.resolve() as? VlangFunctionOrMethodDeclaration ?: return
            val params = function.getSignature()?.parameters?.parametersListWithTypes ?: return

            val isArrayMethod = function is VlangMethodDeclaration && function.receiverType.textMatches("array")

            val param = params.getOrNull(index) ?: return
            val type = param.second?.resolveType() ?: return
            val functionType = if (type is VlangAliasType) {
                val typeList = type.typeUnionList?.typeList ?: return
                if (typeList.size != 1) {
                    return
                }

                typeList.first() as? VlangFunctionType
            } else {
                type as? VlangFunctionType
            }

            if (functionType == null) {
                return
            }

            val presentationText = functionType.text + " {...}"
            val litSignature = functionType.getSignature() ?: return

            result.addElement(
                PrioritizedLookupElement.withPriority(
                    LookupElementBuilder.create("fn")
                        .withPresentableText(presentationText)
                        .withInsertHandler(MyInsertHandler(litSignature, callExpr, isArrayMethod)),
                    VlangCompletionUtil.CONTEXT_COMPLETION_PRIORITY.toDouble()
                )
            )
        }

        class MyInsertHandler(
            private val signature: VlangSignature,
            private val call: VlangCallExpr,
            private val isArrayMethod: Boolean,
        ) : InsertHandler<LookupElement> {

            override fun handleInsert(context: InsertionContext, item: LookupElement) {
                val file = context.file as VlangFile
                val currentModule = file.getModuleQualifiedName()

                val project = signature.project
                val result = signature.result

                processImportTypes(signature, currentModule, file, context.document)

                val templateVariables = processTemplateVars(signature).toMutableList()
                val paramsString = buildParamsPart(signature, call, isArrayMethod, templateVariables)

                val resultTypeEx = result?.type.toEx()
                if (resultTypeEx is VlangVoidPtrTypeEx) {
                    templateVariables.add("T")
                }

                val templateText = buildString {
                    append(" ")
                    append(paramsString)
                    if (result != null) {
                        append(" ")
                        if (resultTypeEx is VlangVoidPtrTypeEx) {
                            append("\$PARAM_T$")
                        } else {
                            append(result.type.toEx().readableName(call))
                        }
                    }
                    append(" {\n\$END\$\n}")
                }

                val template = TemplateManager.getInstance(project)
                    .createTemplate("closures", "vlang", templateText)
                template.isToReformat = true

                templateVariables.forEach {
                    template.addVariable("PARAM_$it", ConstantNode(it), true)
                }

                TemplateManager.getInstance(project).startTemplate(context.editor, template)
            }

            private fun buildParamsPart(
                signature: VlangSignature,
                call: VlangCallExpr,
                isArrayMethod: Boolean,
                templateVariables: List<String>,
            ): String {
                val params = signature.parameters.parametersListWithTypes

                val paramsStrings = params.mapIndexed { index, (def, type) ->
                    val variadic = def?.isVariadic ?: false
                    val modifiers = def?.varModifiers?.text ?: ""
                    val templateVar = templateVariables[index]

                    buildString {
                        if (modifiers.isNotEmpty()) {
                            append(modifiers)
                            append(" ")
                        }
                        append("\$PARAM_$templateVar\$ ")
                        if (variadic) {
                            append("...")
                        }
                        processParamType(type.toEx(), call, isArrayMethod)
                    }
                }

                return buildString {
                    val paramsString = paramsStrings.joinToString(", ") { it }

                    append("(")
                    append(paramsString)
                    append(")")
                }
            }

            private fun StringBuilder.processParamType(
                typeEx: VlangTypeEx<*>,
                call: VlangCallExpr,
                isArrayMethod: Boolean,
            ) {
                if (typeEx is VlangVoidPtrTypeEx && isArrayMethod) {
                    val type = tryInferTypeFromCaller(call)
                    if (type != null) {
                        append(type.readableName(call))
                        return
                    }
                }

                append(typeEx.readableName(call))
            }

            private fun tryInferTypeFromCaller(call: VlangCallExpr): VlangTypeEx<*>? {
                val callerType = VlangTypeInferenceUtil.callerType(call)
                val callerTypeEx = callerType.toEx()
                if (callerTypeEx is VlangArrayTypeEx) {
                    return callerTypeEx.inner
                }

                return null
            }

            private fun processTemplateVars(signature: VlangSignature): List<String> {
                val params = signature.parameters.parametersList.map { it?.name }

                if (params.size == 1) {
                    return listOf(params.first() ?: "it")
                }

                return params.mapIndexed { index, name ->
                    val suffix = if (index == 0) "" else index.toString()
                    name ?: "it$suffix"
                }
            }

            private fun processImportTypes(
                signature: VlangSignature,
                currentModule: String,
                file: VlangFile,
                document: Document,
            ) {
                val typesToImport = findTypesForImport(signature, currentModule)
                if (typesToImport.isEmpty()) {
                    return
                }

                typesToImport.forEach { file.addImport(it.module(), null) }

                PsiDocumentManager.getInstance(file.project).doPostponedOperationsAndUnblockDocument(document)
            }

            private fun findTypesForImport(signature: VlangSignature, currentModule: String): MutableSet<VlangTypeEx<*>> {
                val typesToImport = mutableSetOf<VlangTypeEx<*>>()

                val types = signature.parameters.typeList
                types.forEach { type ->
                    type.toEx().accept(object : VlangTypeVisitor {
                        override fun enter(type: VlangTypeEx<*>): Boolean {
                            if (type is VlangImportableTypeEx) {
                                // type from current module no need to import
                                if (currentModule == type.module()) {
                                    return true
                                }

                                typesToImport.add(type)
                            }

                            return true
                        }
                    })
                }

                return typesToImport
            }
        }
    }
}
