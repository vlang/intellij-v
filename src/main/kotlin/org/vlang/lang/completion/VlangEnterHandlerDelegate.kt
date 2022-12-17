package org.vlang.lang.completion

import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiFile
import com.intellij.psi.util.parentOfType
import org.vlang.lang.psi.VlangFile

// TODO: refactor this completely
class VlangEnterHandlerDelegate : EnterHandlerDelegate {
    private var wasAddedComment = false

    override fun preprocessEnter(
        file: PsiFile,
        editor: Editor,
        caretOffset: Ref<Int>,
        caretAdvance: Ref<Int>,
        dataContext: DataContext,
        originalHandler: EditorActionHandler?,
    ): EnterHandlerDelegate.Result {
        if (file !is VlangFile) {
            return EnterHandlerDelegate.Result.Continue
        }

        val offset = editor.caretModel.currentCaret.offset
        val document = editor.document
        val prevElement = file.findElementAt(offset - 1)?.parentOfType<PsiComment>()

        if (prevElement != null) {
            document.insertString(offset, "// ")
            wasAddedComment = true
            return EnterHandlerDelegate.Result.Continue
        }

        return EnterHandlerDelegate.Result.Continue
    }

    override fun postProcessEnter(file: PsiFile, editor: Editor, dataContext: DataContext): EnterHandlerDelegate.Result {
        if (file !is VlangFile) {
            return EnterHandlerDelegate.Result.Continue
        }

        val offset = editor.caretModel.currentCaret.offset

        if (wasAddedComment) {
            wasAddedComment = false
            editor.caretModel.currentCaret.moveToOffset(offset + 3)
            return EnterHandlerDelegate.Result.Stop
        }

        val document = editor.document
        val line = document.getLineNumber(offset)
        val startOffsetForLine = document.getLineStartOffset(line)
        if (startOffsetForLine == offset) {
            return EnterHandlerDelegate.Result.Continue
        }

        val element = file.findElementAt(offset - 2) ?: return EnterHandlerDelegate.Result.Continue
        if (element.parent is VlangFile) {
            if (offset > editor.document.textLength) {
                return EnterHandlerDelegate.Result.Stop
            }

            editor.document.deleteString(offset - 1, offset)
            return EnterHandlerDelegate.Result.Stop
        }
        return EnterHandlerDelegate.Result.Continue
    }
}
