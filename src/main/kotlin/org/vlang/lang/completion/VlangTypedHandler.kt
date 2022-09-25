package org.vlang.lang.completion

import com.intellij.codeInsight.AutoPopupController
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import org.vlang.lang.psi.VlangFile

class VlangTypedHandler : TypedHandlerDelegate() {
    override fun charTyped(c: Char, project: Project, editor: Editor, file: PsiFile): Result {
        if (file !is VlangFile) {
            return Result.CONTINUE
        }

        val chars = editor.document.charsSequence
        val offset = editor.caretModel.offset

        val prevText = chars.subSequence(offset - "continue ".length, offset).trim()
        if (c == ' ' && (prevText.endsWith("continue") || prevText.endsWith("break") || prevText.endsWith("goto"))) {
            showCompletion(editor)
            return Result.STOP
        }

        return Result.DEFAULT
    }

    private fun showCompletion(editor: Editor) {
        AutoPopupController.getInstance(editor.project!!).autoPopupMemberLookup(editor, null)
    }
}