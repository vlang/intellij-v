package org.vlang.ide.templates.postfix

import com.intellij.codeInsight.template.postfix.templates.PostfixTemplate
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import org.vlang.lang.psi.VlangExpression

abstract class VlangAbstractPrintPostfixTemplate(private val name: String)
    : PostfixTemplate("vlang.postfix.$name", name, "$name(something)", null) {
    override fun isApplicable(context: PsiElement, copyDocument: Document, newOffset: Int) =
        VlangPostfixUtil.isExpression(context) &&
                VlangPostfixUtil.notInsideVarDeclaration(context) &&
                VlangPostfixUtil.notInsideAssignment(context)

    override fun expand(context: PsiElement, editor: Editor) {
        val element = context.parentOfType<VlangExpression>() ?: return
        val document = editor.document
        val caret = editor.caretModel.primaryCaret
        val endOffset = element.endOffset

        document.insertString(endOffset, ")")
        document.insertString(element.startOffset, "$name(")
        caret.moveToOffset(endOffset + name.length + 2)
    }
}

class VlangPrintPostfixTemplate : VlangAbstractPrintPostfixTemplate("print")
class VlangPrintlnPostfixTemplate : VlangAbstractPrintPostfixTemplate("println")
class VlangEprintPostfixTemplate : VlangAbstractPrintPostfixTemplate("eprint")
class VlangEprintlnPostfixTemplate : VlangAbstractPrintPostfixTemplate("eprintln")
class VlangDumpPostfixTemplate : VlangAbstractPrintPostfixTemplate("dump")
