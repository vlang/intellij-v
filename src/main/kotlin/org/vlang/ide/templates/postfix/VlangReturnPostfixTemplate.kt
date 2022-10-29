package org.vlang.ide.templates.postfix

import com.intellij.codeInsight.template.postfix.templates.PostfixTemplate
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import org.vlang.lang.psi.VlangSimpleStatement

class VlangReturnPostfixTemplate : PostfixTemplate(
    "vlang.postfix.return", "ret",
    "return expr", null
) {
    override fun isApplicable(context: PsiElement, copyDocument: Document, newOffset: Int) =
        VlangPostfixUtil.isExpression(context) &&
                VlangPostfixUtil.notInsideVarDeclaration(context) &&
                VlangPostfixUtil.notInsideAssignment(context)

    override fun expand(context: PsiElement, editor: Editor) {
        val element = context.parentOfType<VlangSimpleStatement>() ?: return
        val document = editor.document
        val caret = editor.caretModel.primaryCaret

        document.insertString(element.startOffset, "return ")
        caret.moveToOffset(element.endOffset + 7)
    }
}
