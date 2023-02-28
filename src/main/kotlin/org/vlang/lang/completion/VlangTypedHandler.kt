package org.vlang.lang.completion

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import org.vlang.lang.completion.VlangCompletionUtil.showCompletion
import org.vlang.lang.psi.VlangAttribute
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.VlangReferenceExpression
import org.vlang.utils.inside

class VlangTypedHandler : TypedHandlerDelegate() {
    override fun charTyped(c: Char, project: Project, editor: Editor, file: PsiFile): Result {
        if (file !is VlangFile) {
            return Result.CONTINUE
        }

        val document = editor.document
        val chars = document.charsSequence
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
                document.insertString(offset, "}")
                showCompletion(editor)
                return Result.STOP
            }
        }

        if (c == '}') {
            val nextChar = chars.subSequence(offset, offset + 1).firstOrNull()
            if (nextChar == '}') {
                document.deleteString(offset, offset + 1)
                return Result.STOP
            }
        }

        val prevElement = file.findElementAt(offset - 2)

        if (c == '.' && prevElement != null && prevElement.inside<VlangReferenceExpression>()) {
            showCompletion(editor)
            return Result.STOP
        }

        // compile time constants and functions
        if (c == '$' || c == '@') {
            showCompletion(editor)
            return Result.STOP
        }

        PsiDocumentManager.getInstance(project).commitDocument(document)
        PsiDocumentManager.getInstance(project).doPostponedOperationsAndUnblockDocument(document)

        val parent = file.findElementAt(offset - 1)?.parent
        if (c == '[' && parent is VlangAttribute) {
            showCompletion(editor)
            return Result.STOP
        }

        return Result.DEFAULT
    }
}
