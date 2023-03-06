package org.vlang.ide.templates.postfix

import com.intellij.codeInsight.template.postfix.templates.PostfixTemplateExpressionSelector
import com.intellij.codeInsight.template.postfix.templates.PostfixTemplateWithExpressionSelector
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import org.vlang.ide.templates.postfix.VlangPostfixTemplateProvider.Companion.findAllExpressions
import org.vlang.lang.psi.VlangExpression

class VlangNotPostfixTemplate : PostfixTemplateWithExpressionSelector(
    "vlang.postfix.not", "not",
    "!expr", getBooleanExpressions(), null
) {
    override fun isApplicable(context: PsiElement, copyDocument: Document, newOffset: Int) =
        VlangPostfixUtil.isExpression(context)

    override fun expandForChooseExpression(expression: PsiElement, editor: Editor) {
        val document = editor.document
        val containOtherExpressionInside = PsiTreeUtil.findChildOfType(expression, VlangExpression::class.java) != null

        if (containOtherExpressionInside) {
            document.insertString(expression.startOffset, "!(")
            document.insertString(expression.endOffset + 2, ")")
        } else {
            document.insertString(expression.startOffset, "!")
        }
    }

    companion object {
        private fun getBooleanExpressions(): PostfixTemplateExpressionSelector {
            return findAllExpressions { e -> e is VlangExpression }
        }
    }
}
