package org.vlang.ide.templates

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.lookup.LookupFocusDegree
import com.intellij.codeInsight.template.*
import com.intellij.psi.PsiDocumentManager
import org.vlang.ide.refactoring.rename.VlangNameSuggestionProvider
import org.vlang.lang.psi.VlangTypeOwner

class VlangSuggestVariableNameMacro : Macro() {
    override fun getName(): String = "vlangSuggestVariableName"

    override fun isAcceptableInContext(context: TemplateContextType?): Boolean = context is VlangTemplateContextType

    override fun getLookupFocusDegree(): LookupFocusDegree = LookupFocusDegree.UNFOCUSED

    override fun calculateResult(params: Array<out Expression>, context: ExpressionContext): Result? {
        val names = getNames(context)
        val first = names.firstOrNull() ?: return null
        return TextResult(first)
    }

    override fun calculateLookupItems(params: Array<out Expression>, context: ExpressionContext): Array<LookupElement> {
        return getNames(context).map {
            LookupElementBuilder.create(it)
        }.toTypedArray()
    }

    private fun getNames(context: ExpressionContext): Set<String> {
        val editor = context.editor ?: return emptySet()
        val document = editor.document
        val psiFile = PsiDocumentManager.getInstance(context.project).getPsiFile(document)
        val element = psiFile?.findElementAt(context.startOffset) ?: return emptySet()
        val parent = element.parent as? VlangTypeOwner ?: return emptySet()

        return VlangNameSuggestionProvider.getSuggestedNames(parent)
    }
}