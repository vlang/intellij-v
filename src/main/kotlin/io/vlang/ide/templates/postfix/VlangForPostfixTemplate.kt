package io.vlang.ide.templates.postfix

import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.codeInsight.template.impl.MacroCallNode
import com.intellij.codeInsight.template.postfix.templates.PostfixTemplate
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import com.intellij.psi.util.startOffset
import io.vlang.ide.templates.VlangSuggestVariableNameMacro
import io.vlang.lang.psi.VlangExpression
import io.vlang.lang.psi.types.VlangBaseTypeEx.Companion.unwrapAlias
import io.vlang.lang.psi.types.VlangMapTypeEx

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
        val document = editor.document
        val type = expr.getType(null)?.unwrapAlias()
        val isMap = type is VlangMapTypeEx

        caret.moveToOffset(expr.startOffset)
        document.deleteString(expr.textRange.startOffset, expr.textRange.endOffset)
        val valueVariable = MacroCallNode(VlangSuggestVariableNameMacro())

        if (isMap) {
            VlangPostfixUtil.startTemplate(
                "for \$key$, \$value$ in ${expr.text} {\n\$END$\n}",
                context.project,
                editor,
                "key" to ConstantNode("key"),
                "value" to valueVariable
            )
            return
        }

        VlangPostfixUtil.startTemplate(
            "for \$value$ in ${expr.text} {\n\$END$\n}",
            context.project,
            editor,
            "value" to valueVariable
        )
    }
}
