package org.vlang.ide.templates.postfix

import com.intellij.codeInsight.template.postfix.templates.PostfixTemplate
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import org.vlang.lang.psi.VlangExpression

class VlangArgPostfixTemplate : PostfixTemplate(
    "vlang.postfix.arg", "arg",
    "call(arg)", null
) {
    override fun isApplicable(context: PsiElement, copyDocument: Document, newOffset: Int) =
        VlangPostfixUtil.isExpression(context) &&
                VlangPostfixUtil.notInsideVarDeclaration(context) &&
                VlangPostfixUtil.notInsideAssignment(context)

    override fun expand(context: PsiElement, editor: Editor) {
        val element = context.parentOfType<VlangExpression>() ?: return
        val document = editor.document
        val caret = editor.caretModel.primaryCaret
        val startOffset = element.startOffset

        document.insertString(element.endOffset, ")")
        document.insertString(startOffset, "(")
        caret.moveToOffset(startOffset)
    }
}
