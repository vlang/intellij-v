package io.vlang.ide.templates.postfix

import com.intellij.codeInsight.template.postfix.templates.PostfixTemplateExpressionSelector
import com.intellij.codeInsight.template.postfix.templates.PostfixTemplateWithExpressionSelector
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.util.endOffset
import com.intellij.psi.util.startOffset
import io.vlang.ide.templates.postfix.VlangPostfixTemplateProvider.Companion.findAllExpressions
import io.vlang.lang.psi.VlangExpression

class VlangUnsafePostfixTemplate : PostfixTemplateWithExpressionSelector(
    "vlang.postfix.unsafe", "unsafe",
    "unsafe { expr }", getExpressions(), null
) {
    override fun isApplicable(context: PsiElement, copyDocument: Document, newOffset: Int) =
        VlangPostfixUtil.isExpression(context)

    override fun expandForChooseExpression(expression: PsiElement, editor: Editor) {
        val document = editor.document
        val caret = editor.caretModel.primaryCaret

        document.insertString(expression.startOffset, "unsafe { ")
        document.insertString(expression.endOffset + "unsafe { ".length, " }")
        caret.moveToOffset(expression.endOffset + 2)
    }

    companion object {
        private fun getExpressions(): PostfixTemplateExpressionSelector {
            return findAllExpressions { e -> e is VlangExpression }
        }
    }
}
