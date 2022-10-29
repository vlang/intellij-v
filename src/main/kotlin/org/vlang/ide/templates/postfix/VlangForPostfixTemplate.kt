package org.vlang.ide.templates.postfix

import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.codeInsight.template.postfix.templates.PostfixTemplate
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import com.intellij.refactoring.suggested.startOffset
import org.vlang.lang.psi.VlangSimpleStatement

class VlangForPostfixTemplate : PostfixTemplate(
    "vlang.postfix.for", "for",
    "for value in array", null
) {
    override fun isApplicable(context: PsiElement, copyDocument: Document, newOffset: Int) =
        VlangPostfixUtil.isExpression(context) &&
                VlangPostfixUtil.notInsideVarDeclaration(context)

    override fun expand(context: PsiElement, editor: Editor) {
        val element = context.parentOfType<VlangSimpleStatement>() ?: return
        val caret = editor.caretModel.primaryCaret

        caret.moveToOffset(element.startOffset)
        element.delete()
        PsiDocumentManager.getInstance(context.project).doPostponedOperationsAndUnblockDocument(editor.document)

        VlangPostfixUtil.startTemplate("for \$value$ in ${element.text} {\n\$END$\n}", context.project, editor, "value" to ConstantNode("value"))
    }
}
