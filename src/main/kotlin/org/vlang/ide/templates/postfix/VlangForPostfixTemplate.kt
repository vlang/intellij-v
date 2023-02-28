package org.vlang.ide.templates.postfix

import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.codeInsight.template.postfix.templates.PostfixTemplate
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import com.intellij.refactoring.suggested.startOffset
import org.vlang.lang.psi.VlangExpression
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.unwrapAlias
import org.vlang.lang.psi.types.VlangMapTypeEx

class VlangForPostfixTemplate : PostfixTemplate(
    "vlang.postfix.for", "for",
    "for value in array", null
) {
    override fun isApplicable(context: PsiElement, copyDocument: Document, newOffset: Int) =
        VlangPostfixUtil.isExpression(context) &&
                VlangPostfixUtil.notInsideVarDeclaration(context)

    override fun expand(context: PsiElement, editor: Editor) {
        val expr = context.parentOfType<VlangExpression>() ?: return
        val caret = editor.caretModel.primaryCaret

        val type = expr.getType(null)?.unwrapAlias()
        val isMap = type is VlangMapTypeEx

        caret.moveToOffset(expr.startOffset)
        expr.delete()
        PsiDocumentManager.getInstance(context.project).doPostponedOperationsAndUnblockDocument(editor.document)

        if (isMap) {
            VlangPostfixUtil.startTemplate("for \$key$, \$value$ in ${expr.text} {\n\$END$\n}", context.project, editor, "key" to ConstantNode("key"), "value" to ConstantNode("value"))
            return
        }

        VlangPostfixUtil.startTemplate("for \$value$ in ${expr.text} {\n\$END$\n}", context.project, editor, "value" to ConstantNode("value"))
    }
}
