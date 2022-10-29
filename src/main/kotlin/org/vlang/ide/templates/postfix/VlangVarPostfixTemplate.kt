package org.vlang.ide.templates.postfix

import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.codeInsight.template.postfix.templates.PostfixTemplate
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import com.intellij.refactoring.suggested.startOffset
import org.vlang.lang.psi.VlangSimpleStatement

class VlangVarPostfixTemplate : PostfixTemplate(
    "vlang.postfix.var", "var",
    "var := value", null
) {
    override fun isApplicable(context: PsiElement, copyDocument: Document, newOffset: Int) =
        VlangPostfixUtil.isExpression(context) &&
                VlangPostfixUtil.notInsideVarDeclaration(context)

    override fun expand(context: PsiElement, editor: Editor) {
        val element = context.parentOfType<VlangSimpleStatement>() ?: return
        val caret = editor.caretModel.primaryCaret
        val offset = element.startOffset

        editor.document.insertString(offset, " := ")
        caret.moveToOffset(offset)

        VlangPostfixUtil.startTemplate("\$var$", context.project, editor, "var" to ConstantNode("name"))
    }
}
