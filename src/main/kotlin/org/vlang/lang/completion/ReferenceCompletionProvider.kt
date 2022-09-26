package org.vlang.lang.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionUtilCore
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.ResolveState
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangCachedReference
import org.vlang.lang.psi.impl.VlangReference
import org.vlang.lang.psi.impl.VlangScopeProcessor

class ReferenceCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet,
    ) {
        if (parameters.position.text == CompletionUtilCore.DUMMY_IDENTIFIER_TRIMMED) {
            result.stopHere()
            return
        }

        val expression = parameters.position.parentOfType<VlangReferenceExpressionBase>() ?: return
        val originalFile = parameters.originalFile
        val ref = expression.reference
        if (ref is VlangReference) {
            ref.processResolveVariants(
                MyScopeProcessor(
                    result,
                    originalFile,
                    false
                )
            )
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

    private inner class MyScopeProcessor(
        private val myResult: CompletionResultSet,
        originalFile: PsiFile,
        private val myForTypes: Boolean,
    ) : VlangScopeProcessor() {

        private val processedNames = mutableSetOf<String>()

        override fun execute(o: PsiElement, state: ResolveState): Boolean {
            if (accept(o)) {
                addElement(
                    o,
                    state,
                    myForTypes,
                    processedNames,
                    myResult
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

        return when (element) {
            is VlangFunctionDeclaration  -> VlangCompletionUtil.createFunctionLookupElement(element)
            is VlangMethodDeclaration    -> VlangCompletionUtil.createMethodLookupElement(element)
            is VlangStructDeclaration    -> VlangCompletionUtil.createStructLookupElement(element)
            is VlangUnionDeclaration     -> VlangCompletionUtil.createUnionLookupElement(element)
            is VlangEnumDeclaration      -> VlangCompletionUtil.createEnumLookupElement(element)
            is VlangTypeAliasDeclaration -> VlangCompletionUtil.createTypeAliasLookupElement(element)
            is VlangFieldDefinition      -> VlangCompletionUtil.createFieldLookupElement(element)
            else                         -> VlangCompletionUtil.createVariableLikeLookupElement(element)
        }
    }
}
