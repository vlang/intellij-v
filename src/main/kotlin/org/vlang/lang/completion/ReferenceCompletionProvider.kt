package org.vlang.lang.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.ResolveState
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.*

class ReferenceCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet,
    ) {
//        if (parameters.position.text == CompletionUtilCore.DUMMY_IDENTIFIER_TRIMMED) {
//            result.stopHere()
//            return
//        }

        val element = parameters.position

        if (VlangCompletionUtil.isCompileTimeIdentifier(element)) {
            fillCompileTimeConstantsVariants(result)
            return
        }

        val expression = element.parentOfType<VlangReferenceExpressionBase>() ?: return
        val originalFile = parameters.originalFile
        val ref = expression.reference
        if (ref is VlangReference) {
            val refExpression = ref.element as? VlangReferenceExpression
            val variants = VlangStructLiteralCompletion.allowedVariants(refExpression)

            fillStructFieldNameVariants(originalFile, result, variants, refExpression)

            if (variants != VlangStructLiteralCompletion.Variants.FIELD_NAME_ONLY) {
                ref.processResolveVariants(
                    MyScopeProcessor(
                        result,
                        originalFile,
                        false
                    )
                )
            }
        } else if (ref is VlangCachedReference<*>) {
            ref.processResolveVariants(
                MyScopeProcessor(
                    result,
                    originalFile,
                    false
                )
            )
        }
    }

    private fun fillCompileTimeConstantsVariants(result: CompletionResultSet) {
        result.addAllElements(
            VlangCompletionUtil.compileTimeConstants.map {
                VlangCompletionUtil.createCompilePseudoVarLookupElement(it.key, it.value)
            }
        )
    }

    private fun fillStructFieldNameVariants(
        file: PsiFile,
        result: CompletionResultSet,
        variants: VlangStructLiteralCompletion.Variants,
        refExpression: VlangReferenceExpression?,
    ) {
        if (refExpression == null ||
            variants !== VlangStructLiteralCompletion.Variants.FIELD_NAME_ONLY &&
            variants !== VlangStructLiteralCompletion.Variants.BOTH
        ) {
            return
        }

        val fields = mutableSetOf<String>()
        val literal = refExpression.parentOfType<VlangLiteralValueExpression>()
        VlangFieldNameReference(refExpression).processResolveVariants(object : MyScopeProcessor(result, file, false) {
            val alreadyAssignedFields: Set<String> = VlangStructLiteralCompletion.alreadyAssignedFields(literal)

            override fun execute(o: PsiElement, state: ResolveState): Boolean {
                val structFieldName: String? =
                    if (o is VlangFieldDefinition)
                        o.name
                    else if (o is VlangAnonymousFieldDefinition)
//                        o.getName() // TODO
                        ""
                    else null

                if (structFieldName != null) {
                    fields.add(structFieldName)
                }

                return if (structFieldName != null && alreadyAssignedFields.contains(structFieldName)) {
                    true
                } else {
                    super.execute(o, state)
                }
            }
        })

        // TODO
//        result.addElement(
//            PrioritizedLookupElement.withPriority(
//                LookupElementBuilder.create("")
//                    .withPresentableText("Fill all field...")
//                    .withInsertHandler { context, item ->
//                        val offset = context.editor.caretModel.offset
//                        val element = context.file.findElementAt(offset)?.parent ?: return@withInsertHandler
//
//                        val line = context.document.getLineNumber(offset)
//                        val startLineOffset = context.document.getLineStartOffset(line)
//                        val shift = offset - startLineOffset
//
////                        val data = fields.joinToString("\n") { " ".repeat(shift + 1) + it + ": " }
////                        context.document.insertString(offset, "\n$data\n" + " ".repeat(shift))
//
////                        val editorEx = context.editor as EditorEx
////                        editorEx.setPlaceholder("name")
//
//                        val manager = TemplateManager.getInstance(context.project)
//                        val template = manager.createTemplate("", "", "\$name\$")
//
//                        val builder = TemplateBuilderImpl(element)
//                        builder.replaceElement(
//                            element,
//                            TextRange(0, 2), "var", ConstantNode("name"), true
//                        )
//
//                        builder.initInlineTemplate(template)
//                    },
//                10000.0
//            )
//        )
    }

    private open inner class MyScopeProcessor(
        private val result: CompletionResultSet,
        originalFile: PsiFile,
        private val forTypes: Boolean,
    ) : VlangScopeProcessor() {

        private val processedNames = mutableSetOf<String>()

        override fun execute(o: PsiElement, state: ResolveState): Boolean {
            if (accept(o)) {
                addElement(
                    o,
                    state,
                    forTypes,
                    processedNames,
                    result
                )
            }
            return true
        }

        private fun accept(e: PsiElement) = true

        override fun isCompletion(): Boolean = true
    }

    private fun addElement(
        o: PsiElement,
        state: ResolveState,
        forTypes: Boolean,
        processedNames: MutableSet<String>,
        set: CompletionResultSet,
    ) {
        val lookup = createLookupElement(o, state, forTypes)
        if (lookup != null) {
            val lookupString = lookup.lookupString
            if (!processedNames.contains(lookupString)) {
                set.addElement(lookup)
                processedNames.add(lookupString)
            }
        }
    }

    private fun createLookupElement(
        element: PsiElement,
        state: ResolveState,
        forTypes: Boolean,
    ): LookupElement? {
        if (element !is VlangNamedElement || element.isBlank()) return null

        if (element is VlangImportSpec) {
            //                    return VlangCompletionUtil.createPackageLookupElement(
            //                        o as VlangImportSpec,
            //                        state[VlangReferenceBase.ACTUAL_NAME],
            //                        vendoringEnabled
            //                    )
        } else if (element is VlangNamedSignatureOwner && element.name != null) {
            val name = element.name
            if (name != null) {
                //                        return VlangCompletionUtil.createFunctionOrMethodLookupElement(
                //                            o as VlangNamedSignatureOwner, name, null,
                //                            VlangCompletionUtil.FUNCTION_PRIORITY
                //                        )
            }
        } else if (element is PsiDirectory) {
            //                    return VlangCompletionUtil.createPackageLookupElement(o.name, o, o, vendoringEnabled, true)
        } else if (element is VlangLabelDefinition) {
            val name = element.name
            //                    if (name != null) return VlangCompletionUtil.createLabelLookupElement(o as VlangLabelDefinition, name)
        }

        val moduleName = state.get(VlangReferenceBase.MODULE_NAME)

        return when (element) {
            is VlangFunctionDeclaration  -> VlangCompletionUtil.createFunctionLookupElement(element, moduleName)
            is VlangMethodDeclaration    -> VlangCompletionUtil.createMethodLookupElement(element)
            is VlangStructDeclaration    -> VlangCompletionUtil.createStructLookupElement(element, moduleName)
            is VlangUnionDeclaration     -> VlangCompletionUtil.createUnionLookupElement(element, moduleName)
            is VlangEnumDeclaration      -> VlangCompletionUtil.createEnumLookupElement(element, moduleName)
            is VlangInterfaceDeclaration -> VlangCompletionUtil.createInterfaceLookupElement(element, moduleName)
            is VlangTypeAliasDeclaration -> VlangCompletionUtil.createTypeAliasLookupElement(element, moduleName)
            is VlangFieldDefinition      -> VlangCompletionUtil.createFieldLookupElement(element)
            is VlangConstDefinition      -> VlangCompletionUtil.createConstantLookupElement(element)
            is VlangEnumFieldDefinition  -> VlangCompletionUtil.createEnumFieldLookupElement(element)
            else                         -> VlangCompletionUtil.createVariableLikeLookupElement(element)
        }
    }
}
