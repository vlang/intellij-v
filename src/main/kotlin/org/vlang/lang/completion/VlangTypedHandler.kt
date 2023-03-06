package org.vlang.lang.completion

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.TypedAction
import com.intellij.openapi.editor.impl.TypedActionImpl
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.elementType
import com.intellij.psi.util.parentOfType
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import org.vlang.ide.editor.VlangEditorUtil
import org.vlang.lang.VlangTypes
import org.vlang.lang.completion.VlangCompletionUtil.showCompletion
import org.vlang.lang.psi.*
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

        if (c == ',') {
            if (isAfterFunctionResultType(editor, offset)) {
                PsiDocumentManager.getInstance(project).commitDocument(document)
                val element = file.findElementAt(offset - 2)
                val resultElement = element?.parentOfType<VlangResult>()
                if (resultElement != null && !resultElement.isVoid) {
                    val lparenOffset = resultElement.startOffset
                    beginUndoablePostProcessing()
                    document.insertString(lparenOffset, "(")
                    document.insertString(editor.caretModel.offset, ")")
                }
                return Result.STOP
            }
        }

        if (c == ':' && prevElement != null) {
            if (prevElement.elementType == VlangTypes.MUT || prevElement.elementType == VlangTypes.PUB) {
                val modifiers = prevElement.parentOfType<VlangUnfinishedMemberModifiers>() ?: return Result.DEFAULT
                val fieldsGroup = modifiers.parent
                val beforeGroup = fieldsGroup.prevSibling
                if (beforeGroup is PsiWhiteSpace && !beforeGroup.text.contains("\n")) {
                    document.deleteString(beforeGroup.startOffset, beforeGroup.endOffset)
                }
            }
        }

        if (c == '[') {
            PsiDocumentManager.getInstance(project).commitDocument(document)
            PsiDocumentManager.getInstance(project).doPostponedOperationsAndUnblockDocument(document)

            val parent = file.findElementAt(offset - 1)?.parent
            if (parent is VlangAttribute) {
                showCompletion(editor)
                return Result.STOP
            }
        }

        return Result.DEFAULT
    }

    private fun isAfterFunctionResultType(editor: Editor, offset: Int): Boolean {
        if (offset == 0) return false
        if (offset >= editor.document.textLength) return false
        val iterator = editor.highlighter.createIterator(offset)
        var token = VlangEditorUtil.skipWhitespacesForward(iterator)
        if (token != VlangTypes.LBRACE && token != VlangTokenTypes.NLS) {
            return false
        }

        val beforeIterator = editor.highlighter.createIterator(offset - 1)
        beforeIterator.retreat()

        val beforeToken = beforeIterator.tokenType
        if (beforeToken == VlangTypes.RPAREN) {
            // when fn () (int, string)<caret>
            // ->  fn () (int, string),
            return false
        }

        // check presence of fn keyword in same line
        beforeIterator.advance()
        while (!beforeIterator.atEnd()) {
            token = beforeIterator.tokenType

            if (token == VlangTokenTypes.NLS) {
                return true
            }

            if (token == VlangTypes.FN) {
                return true
            }

            beforeIterator.retreat()
        }

        return false
    }

    private fun beginUndoablePostProcessing() {
        (TypedAction.getInstance() as TypedActionImpl).defaultRawTypedHandler.beginUndoablePostProcessing()
    }
}
