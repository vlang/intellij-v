package io.vlang.ide.templates.postfix

import com.intellij.codeInsight.template.postfix.templates.PostfixTemplateExpressionSelector
import com.intellij.codeInsight.template.postfix.templates.PostfixTemplateWithExpressionSelector
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import io.vlang.ide.templates.postfix.VlangPostfixTemplateProvider.Companion.findAllExpressions
import io.vlang.lang.psi.VlangExpression

class VlangArgPostfixTemplate : PostfixTemplateWithExpressionSelector(
    "vlang.postfix.arg", "arg",
    "call(arg)", getExpressions(), null
) {
    override fun isApplicable(context: PsiElement, copyDocument: Document, newOffset: Int) =
        VlangPostfixUtil.isExpression(context)

    override fun expandForChooseExpression(expression: PsiElement, editor: Editor) {
        val document = editor.document
        val caret = editor.caretModel.primaryCaret

        document.insertString(expression.endOffset, ")")
        document.insertString(expression.startOffset, "(")
        caret.moveToOffset(expression.startOffset)
    }

    companion object {
        private fun getExpressions(): PostfixTemplateExpressionSelector {
            return findAllExpressions { e -> e is VlangExpression }
        }
    }
}
