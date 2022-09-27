package org.vlang.lang.completion

import com.intellij.codeInsight.AutoPopupController
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.util.parentOfType
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.VlangReferenceExpression

class VlangTypedHandler : TypedHandlerDelegate() {
    override fun charTyped(c: Char, project: Project, editor: Editor, file: PsiFile): Result {
        if (file !is VlangFile) {
            return Result.CONTINUE
        }

        val chars = editor.document.charsSequence
        val offset = editor.caretModel.offset

        if (offset > 10) {
            val prevText = chars.subSequence(offset - "continue ".length, offset).trim()
            if (c == ' ' && (prevText.endsWith("continue") || prevText.endsWith("break") || prevText.endsWith("goto"))) {
                showCompletion(editor)
                return Result.STOP
            }
        }

        if (offset > 2) {
            val prevSymbol = chars.subSequence(offset - 2, offset - 1).first()
            if (c == '{' && prevSymbol == '$') {
                editor.document.insertString(offset, "}")
                showCompletion(editor)
                return Result.STOP
            }
        }

        val prevElement = file.findElementAt(offset - 2)

        if (c == '.' && prevElement?.parentOfType<VlangReferenceExpression>() != null) {
            showCompletion(editor)
            return Result.STOP
        }

        return Result.DEFAULT
    }

    private fun showCompletion(editor: Editor) {
        AutoPopupController.getInstance(editor.project!!).autoPopupMemberLookup(editor, null)
    }
}