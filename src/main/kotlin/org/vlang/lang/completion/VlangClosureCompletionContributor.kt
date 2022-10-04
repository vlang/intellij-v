package org.vlang.lang.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.*
import org.vlang.utils.parentNum

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
            val callExpr = pos.parentNum<VlangCallExpr>(5) ?: return
            val element = pos.parentOfType<VlangElement>()
            val args = callExpr.argumentList.elementList
            val index = args.indexOf(element)
            if (index == -1) {
                return
            }

            val refExpr = callExpr.expression as? VlangReferenceExpression ?: return
            val function = refExpr.resolve() as? VlangFunctionOrMethodDeclaration ?: return
            val params = function.getSignature()?.parameters?.parametersListWithTypes ?: return

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
            val litSignature = functionType.signature ?: return

            result.addElement(
                PrioritizedLookupElement.withPriority(
                    LookupElementBuilder.create("fn")
                        .withPresentableText(presentationText)
                        .withInsertHandler(MyInsertHandler(litSignature)),
                    VlangCompletionUtil.CONTEXT_COMPLETION_PRIORITY.toDouble()
                )
            )
        }

        class MyInsertHandler(private val signature: VlangSignature) : InsertHandler<LookupElement> {
            override fun handleInsert(context: InsertionContext, item: LookupElement) {
                val editor = context.editor
                val project = signature.project
                val params = signature.parameters.parametersListWithTypes
                val result = signature.result

                val templateVariables = params.mapIndexed { index, (def, _) ->
                    def?.name ?: "arg$index"
                }

                val paramsMap = params.mapIndexed { index, (_, type) ->
                    "\$PARAM_${templateVariables[index]}\$ " + type.readableName
                }

                val paramsString = buildString {
                    val paramsString = paramsMap.joinToString(", ") { it }

                    append("(")
                    append(paramsString)
                    append(")")
                }

                val templateText = buildString {
                    append(" ")
                    append(paramsString)
                    if (result != null) {
                        append(" ")
                        append(result.type.readableName)
                    }
                    append(" {\n\$END\$\n}")
                }

                val template = TemplateManager.getInstance(project)
                    .createTemplate("braces", "vlang", templateText)
                template.isToReformat = true

                templateVariables.forEach {
                    template.addVariable("PARAM_$it", ConstantNode(it), true)
                }

                TemplateManager.getInstance(project).startTemplate(editor, template)
            }
        }
    }
}
