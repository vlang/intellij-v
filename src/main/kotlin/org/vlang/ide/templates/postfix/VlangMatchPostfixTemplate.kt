package org.vlang.ide.templates.postfix

import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.codeInsight.template.postfix.templates.PostfixTemplate
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import com.intellij.refactoring.suggested.startOffset
import org.vlang.lang.psi.VlangSimpleStatement

class VlangMatchPostfixTemplate : PostfixTemplate(
    "vlang.postfix.match", "match",
    "match expr {}", null
) {
    override fun isApplicable(context: PsiElement, copyDocument: Document, newOffset: Int) =
        VlangPostfixUtil.isExpression(context) &&
                VlangPostfixUtil.notInsideVarDeclaration(context) &&
                VlangPostfixUtil.notInsideAssignment(context)

    override fun expand(context: PsiElement, editor: Editor) {
        val element = context.parentOfType<VlangSimpleStatement>() ?: return
        val document = editor.document

        document.insertString(element.startOffset, "match ")

        VlangPostfixUtil.startTemplate(
            " {\n \$cond$ { \$END$ }\n}", context.project, editor,
            "cond" to ConstantNode("cond"),
        )
    }
}
