package org.vlang.ide.templates.postfix

import com.intellij.codeInsight.template.postfix.templates.PostfixTemplateExpressionSelector
import com.intellij.codeInsight.template.postfix.templates.PostfixTemplateWithExpressionSelector
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import org.vlang.ide.templates.postfix.VlangPostfixTemplateProvider.Companion.findAllExpressions
import org.vlang.lang.psi.VlangExpression

class VlangParPostfixTemplate : PostfixTemplateWithExpressionSelector(
    "vlang.postfix.par", "par",
    "(expr)", getExpressions(), null
) {
    override fun isApplicable(context: PsiElement, copyDocument: Document, newOffset: Int) =
        VlangPostfixUtil.isExpression(context)

    override fun expandForChooseExpression(expression: PsiElement, editor: Editor) {
        val document = editor.document
        val caret = editor.caretModel.primaryCaret

        document.insertString(expression.endOffset, ")")
        document.insertString(expression.startOffset, "(")
        caret.moveToOffset(expression.endOffset + 2)
    }

    companion object {
        private fun getExpressions(): PostfixTemplateExpressionSelector {
            return findAllExpressions { e -> e is VlangExpression }
        }
    }
}
