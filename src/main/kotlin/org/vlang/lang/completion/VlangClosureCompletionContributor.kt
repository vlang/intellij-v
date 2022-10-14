package org.vlang.lang.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.*
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.lang.psi.types.VlangImportableType
import org.vlang.lang.psi.types.VlangTypeEx
import org.vlang.lang.psi.types.VlangTypeVisitor
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
            val litSignature = functionType.getSignature() ?: return

            result.addElement(
                PrioritizedLookupElement.withPriority(
                    LookupElementBuilder.create("fn")
                        .withPresentableText(presentationText)
                        .withInsertHandler(MyInsertHandler(litSignature, callExpr)),
                    VlangCompletionUtil.CONTEXT_COMPLETION_PRIORITY.toDouble()
                )
            )
        }

        class MyInsertHandler(private val signature: VlangSignature, private val position: VlangCompositeElement) : InsertHandler<LookupElement> {
            override fun handleInsert(context: InsertionContext, item: LookupElement) {
                val editor = context.editor
                val file = context.file as VlangFile
                val module = file.getModuleQualifiedName()

                val project = signature.project
                val params = signature.parameters.parametersListWithTypes
                val result = signature.result

                val typesToImport = mutableSetOf<VlangTypeEx<*>>()
                params.forEach { (_, type) ->
                    type.toEx().accept(object : VlangTypeVisitor {
                        override fun enter(type: VlangTypeEx<*>): Boolean {
                            if (type is VlangImportableType) {
                                // type from current module no need to import
                                if (module == type.module()) {
                                    return true
                                }

                                typesToImport.add(type)
                            }

                            return true
                        }
                    })
                }

                typesToImport.forEach {
                    val name = it.module()
                    file.addImport(name, null)
                }

                PsiDocumentManager.getInstance(project).doPostponedOperationsAndUnblockDocument(context.document)

                val templateVariables = params.mapIndexed { index, (def, _) ->
                    def?.name ?: "arg$index"
                }

                val paramsMap = params.mapIndexed { index, (def, type) ->
                    val variadic = def.isVariadic
                    val modifiers = def.varModifiers?.text ?: ""

                    buildString {
                        if (modifiers.isNotEmpty()) {
                            append(modifiers)
                            append(" ")
                        }
                        append("\$PARAM_${templateVariables[index]}\$ ")
                        if (variadic) {
                            append("...")
                        }
                        append(type.toEx().readableName(position))
                    }
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
                        append(result.type.toEx().readableName(position))
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
