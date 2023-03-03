package org.vlang.ide.templates.postfix

import com.intellij.codeInsight.template.impl.MacroCallNode
import com.intellij.codeInsight.template.postfix.templates.PostfixTemplate
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import com.intellij.refactoring.suggested.startOffset
import org.vlang.ide.templates.VlangSuggestVariableNameMacro
import org.vlang.lang.psi.VlangExpression

class VlangForIndexedPostfixTemplate : PostfixTemplate(
    "vlang.postfix.fori", "fori",
    "for index, value in array", null
) {
    override fun isApplicable(context: PsiElement, copyDocument: Document, newOffset: Int) =
        VlangPostfixUtil.isExpression(context) &&
                VlangPostfixUtil.notInsideVarDeclaration(context)

    override fun expand(context: PsiElement, editor: Editor) {
        val expr = context.parentOfType<VlangExpression>() ?: return
        val caret = editor.caretModel.primaryCaret
        val document = editor.document

        caret.moveToOffset(expr.startOffset)
        document.deleteString(expr.textRange.startOffset, expr.textRange.endOffset)

        val indexVariable = MacroCallNode(VlangSuggestVariableNameMacro())
        val valueVariable = MacroCallNode(VlangSuggestVariableNameMacro())

        VlangPostfixUtil.startTemplate(
            "for \$key$, \$value$ in ${expr.text} {\n\$END$\n}",
            context.project,
            editor,
            "key" to indexVariable,
            "value" to valueVariable
        )
    }
}
