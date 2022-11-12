package org.vlang.lang.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.Document
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.ide.codeInsight.VlangTypeInferenceUtil
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.*
import org.vlang.lang.psi.types.*
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx

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

            val contextType = VlangTypeInferenceUtil.getContextType(pos.parent)

            val functionType = if (contextType is VlangAliasType) {
                contextType.aliasType
            } else {
                contextType
            } as? VlangFunctionType ?: return

            val presentationText = functionType.text + " {...}"
            val signature = functionType.getSignature() ?: return

            val callExpr = VlangCodeInsightUtil.getCallExpr(pos)
            result.addElement(
                PrioritizedLookupElement.withPriority(
                    LookupElementBuilder.create("fn")
                        .withPresentableText(presentationText)
                        .withIcon(AllIcons.Actions.RealIntentionBulb)
                        .withInsertHandler(MyInsertHandler(signature, pos, callExpr)),
                    VlangCompletionUtil.CONTEXT_COMPLETION_PRIORITY.toDouble()
                )
            )
        }

        class MyInsertHandler(
            private val signature: VlangSignature,
            private val anchor: PsiElement,
            private val call: VlangCallExpr?,
        ) : InsertHandler<LookupElement> {

            override fun handleInsert(context: InsertionContext, item: LookupElement) {
                val file = context.file as VlangFile
                val currentModule = file.getModuleQualifiedName()

                val project = signature.project
                val result = signature.result

                processImportTypes(signature, currentModule, file, context.document)

                val templateVariables = processTemplateVars(signature).toMutableList()
                val paramsString = buildParamsPart(anchor, signature, call, templateVariables)

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
                            append(result.type.toEx().readableName(anchor))
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
                anchor: PsiElement,
                signature: VlangSignature,
                call: VlangCallExpr?,
                templateVariables: List<String>,
            ): String {
                val params = signature.parameters.paramDefinitionList

                val paramsStrings = params.mapIndexed { index, param ->
                    val variadic = param.isVariadic
                    val modifiers = param.varModifiers?.text ?: ""
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
                        processParamType(param.type.toEx(), anchor, call)
                    }
                }

                return buildString {
                    val paramsString = paramsStrings.joinToString(", ") { it }

                    append("(")
                    append(paramsString)
                    append(")")
                }
            }

            private fun StringBuilder.processParamType(typeEx: VlangTypeEx<*>, anchor: PsiElement, call: VlangCallExpr?) {
                if (call != null && typeEx is VlangVoidPtrTypeEx && VlangCodeInsightUtil.isArrayMethodCall(call)) {
                    val type = tryInferTypeFromCaller(call)
                    if (type != null) {
                        append(type.readableName(call))
                        return
                    }
                }

                append(typeEx.readableName(anchor))
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
                val paramNames = signature.parameters.paramDefinitionList.map { it?.name }

                if (paramNames.size == 1) {
                    return listOf(paramNames.first() ?: "it")
                }

                return paramNames.mapIndexed { index, name ->
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

                val types = signature.parameters.paramDefinitionList.mapNotNull { it.type }
                types.forEach { type ->
                    type.toEx().accept(object : VlangTypeVisitor {
                        override fun enter(type: VlangTypeEx<*>): Boolean {
                            if (type is VlangImportableTypeEx) {
                                // type from current module no need to import
                                if (currentModule == type.module() || type.isBuiltin()) {
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
