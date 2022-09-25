package org.vlang.lang.completion

import com.intellij.codeInsight.editorActions.BackspaceHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import org.vlang.lang.psi.VlangFile

class VlangBackspaceHandlerDelegate : BackspaceHandlerDelegate() {
    override fun beforeCharDeleted(c: Char, file: PsiFile, editor: Editor) {

    }

    override fun charDeleted(c: Char, file: PsiFile, editor: Editor): Boolean {
        val offset = editor.caretModel.currentCaret.offset
        val element = file.findElementAt(offset - 1) ?: return false
        if (element.parent is VlangFile) {
            val prev = editor.document.charsSequence.subSequence(offset - 1, offset)
            if (prev == "\t") {
                editor.document.deleteString(offset - 1, offset)
            }
            return true
        }
        return false
    }
}
