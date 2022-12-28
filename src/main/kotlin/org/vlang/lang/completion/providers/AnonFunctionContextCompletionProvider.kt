package org.vlang.lang.completion.providers

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.icons.AllIcons
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.ide.codeInsight.VlangGenericInferer
import org.vlang.ide.codeInsight.VlangTypeInferenceUtil
import org.vlang.lang.completion.VlangCompletionUtil.CONTEXT_COMPLETION_PRIORITY
import org.vlang.lang.completion.VlangCompletionUtil.withPriority
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangLangUtil
import org.vlang.lang.psi.types.*
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.getGenericTs
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.isGeneric
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx

object AnonFunctionContextCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        val pos = parameters.position
        val contextType = VlangTypeInferenceUtil.getContextType(pos.parent)

        val functionType = if (contextType is VlangAliasTypeEx) {
            contextType.inner
        } else {
            contextType
        } as? VlangFunctionTypeEx ?: return

        val presentationText = "$functionType {...}"
        val signature = functionType.signature

        val element = LookupElementBuilder.create("fn")
            .withPresentableText(presentationText)
            .withIcon(AllIcons.Actions.RealIntentionBulb)
            .withInsertHandler(MyInsertHandler(signature, pos))
            .withPriority(CONTEXT_COMPLETION_PRIORITY)

        result.addElement(element)
    }

    class MyInsertHandler(private val signature: VlangSignature, private val anchor: PsiElement) : InsertHandler<LookupElement> {
        override fun handleInsert(context: InsertionContext, item: LookupElement) {
            val file = context.file as VlangFile

            val project = signature.project
            val result = signature.result

            VlangLangUtil.importTypesFromSignature(signature, file)

            val (call, argumentsOwner, parametersOwner) = calculateOwners(project)

            val genericParameters = getGenericParameters(argumentsOwner, parametersOwner)
            val inferredGenericTs = getGenericTs(argumentsOwner, parametersOwner)

            val unresolvedGenericTypes = mutableListOf<String>()
            genericParameters.forEach {
                if (it !in inferredGenericTs) {
                    unresolvedGenericTypes.add(it)
                }
            }

            val templateParamNames = processTemplateVars(signature).toMutableList()

            val paramsString = processParams(anchor, signature, call, templateParamNames, inferredGenericTs)

            val resultTypeEx = result?.type.toEx()
            if (resultTypeEx is VlangVoidPtrTypeEx) {
                templateParamNames.add("T")
            }

            val templateText = buildString {
                append(" ")
                append(paramsString)
                if (result != null) {
                    append(" ")
                    if (resultTypeEx is VlangVoidPtrTypeEx) {
                        append("\$PARAM_T$")
                    } else {
                        processType(anchor, result.type.toEx(), call, inferredGenericTs)
                    }
                }
                append(" {\n\$END\$\n}")
            }

            val template = TemplateManager.getInstance(project)
                .createTemplate("closures", "vlang", templateText)
            template.isToReformat = true

            templateParamNames.forEach {
                template.addVariable("PARAM_$it", createTemplateConstantNode(it), true)
            }

            unresolvedGenericTypes.forEach {
                template.addVariable("PARAM_TYPE_$it", createTemplateConstantNode(it), true)
            }

            TemplateManager.getInstance(project).startTemplate(context.editor, template)
        }

        private fun calculateOwners(project: Project): Triple<VlangCallExpr?, VlangGenericArgumentsOwner?, VlangGenericParametersOwner?> {
            val call = VlangCodeInsightUtil.getCallExpr(anchor)
            val structInit = VlangCodeInsightUtil.getLiteralValueExpr(anchor)
            val structType = structInit?.type
            val structTypeEx = structInit?.type?.toEx() as? VlangResolvableTypeEx<*>
            val structDeclaration = structTypeEx?.resolve(project) as? VlangStructDeclaration

            val callRef = call?.expression as? VlangReferenceExpression
            val function = callRef?.reference?.resolve() as? VlangSignatureOwner

            val argumentsOwner = if (call != null && structInit != null) {
                val callParentOfStructInit = PsiTreeUtil.isAncestor(call, structInit, false)
                if (callParentOfStructInit) {
                    structType
                } else {
                    call
                }
            } else {
                call ?: structType
            }

            val parametersOwner = (if (argumentsOwner is VlangCallExpr) function else structDeclaration?.structType)

            return Triple(call, argumentsOwner, parametersOwner)
        }

        private fun createTemplateConstantNode(it: String): ConstantNode {
            if (ApplicationManager.getApplication().isUnitTestMode) {
                return ConstantNode("EXPECTED_USER_INPUT_FOR_$it")
            }

            return ConstantNode(it)
        }

        private fun getGenericTs(
            argumentsOwner: VlangGenericArgumentsOwner?,
            parametersOwner: VlangGenericParametersOwner?,
        ) =
            if (argumentsOwner != null && parametersOwner != null) {
                VlangGenericInferer.inferGenericTsMap(argumentsOwner, parametersOwner)
            } else {
                emptyMap()
            }

        private fun getGenericParameters(
            argumentsOwner: VlangGenericArgumentsOwner?,
            parametersOwner: VlangGenericParametersOwner?,
        ) =
            if (argumentsOwner != null && parametersOwner != null) {
                VlangGenericInferer.getGenericParameters(argumentsOwner, parametersOwner)
            } else {
                emptyList()
            }

        private fun processParams(
            anchor: PsiElement,
            signature: VlangSignature,
            call: VlangCallExpr?,
            templateParamNames: List<String>,
            genericTs: Map<String, VlangTypeEx>,
        ): String {
            val params = signature.parameters.paramDefinitionList

            val paramsStrings = params.mapIndexed { index, param ->
                val variadic = param.isVariadic
                val modifiers = param.varModifiers?.text ?: ""
                val templateVar = templateParamNames[index]

                buildString {
                    if (modifiers.isNotEmpty()) {
                        append(modifiers)
                        append(" ")
                    }
                    append("\$PARAM_$templateVar$ ")
                    if (variadic) {
                        append("...")
                    }
                    processType(anchor, param.type.toEx(), call, genericTs)
                }
            }

            return buildString {
                val paramsString = paramsStrings.joinToString(", ") { it }

                append("(")
                append(paramsString)
                append(")")
            }
        }

        private fun StringBuilder.processType(
            anchor: PsiElement,
            type: VlangTypeEx,
            call: VlangCallExpr?,
            genericTs: Map<String, VlangTypeEx>,
        ) {
            if (call != null && type is VlangVoidPtrTypeEx && VlangCodeInsightUtil.isArrayMethodCall(call)) {
                val inferredType = tryInferTypeFromCaller(call)
                if (inferredType != null) {
                    append(inferredType.readableName(call))
                    return
                }
            }

            if (type.isGeneric()) {
                val inferredType = type.substituteGenerics(genericTs)

                // still generic, some T is not inferred
                if (inferredType.isGeneric()) {
                    val unresolvedGenericTs = type.getGenericTs()
                    val unresolvedGenericTsMap = unresolvedGenericTs
                        .associateWith { VlangStructTypeEx("\$PARAM_TYPE_$it$", type.anchor()) }
                    val templateType = type.substituteGenerics(unresolvedGenericTsMap)

                    val inferredTypeString = templateType.toString()
                    append(inferredTypeString)
                    return
                }

                append(inferredType.readableName(anchor))
                return
            }

            append(type.readableName(anchor))
        }

        private fun tryInferTypeFromCaller(call: VlangCallExpr): VlangTypeEx? {
            val callerType = VlangTypeInferenceUtil.callerType(call)
            if (callerType is VlangArrayTypeEx) {
                return callerType.inner
            }

            return null
        }

        private fun processTemplateVars(signature: VlangSignature): List<String> {
            val paramNames = signature.parameters.paramDefinitionList.map { it?.name }

            if (paramNames.size == 1) {
                return listOf(paramNames.first() ?: "it")
            }

            return paramNames.mapIndexed { index, name ->
                val suffix = if (index == 0) "" else index.toString()
                name ?: "it$suffix"
            }
        }
    }
}
