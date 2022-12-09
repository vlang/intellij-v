package org.vlang.lang.completion

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import org.vlang.lang.completion.VlangCompletionUtil.showCompletion
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.VlangReferenceExpression
import org.vlang.utils.inside

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

        if (c == '.' && prevElement != null && prevElement.inside<VlangReferenceExpression>()) {
            showCompletion(editor)
            return Result.STOP
        }

        if (c == '[') {
            showCompletion(editor)
            return Result.STOP
        }

        // compile time constants and functions
        if (c == '$' || c == '@') {
            showCompletion(editor)
            return Result.STOP
        }

        return Result.DEFAULT
    }
}
