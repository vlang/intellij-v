package org.vlang.lang.completion

import com.intellij.codeInsight.completion.BasicInsertHandler
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.lookup.LookupElement

open class SingleCharInsertHandler(private val char: Char, private val needSpace: Boolean = false) : BasicInsertHandler<LookupElement>() {
    override fun handleInsert(context: InsertionContext, item: LookupElement) {
        val editor = context.editor
        val tailOffset = context.tailOffset
        val document = editor.document
        context.commitDocument()

        val staysAtChar = document.textLength > tailOffset &&
                document.charsSequence[tailOffset] == char
        context.setAddCompletionChar(false)

        if (!staysAtChar) {
            document.insertString(tailOffset, char.toString())
        }

        editor.caretModel.moveToOffset(tailOffset + 1)

        if (needSpace) {
            document.insertString(tailOffset + 1, " ")
            editor.caretModel.moveToOffset(tailOffset + 2)
        }

        VlangCompletionUtil.showCompletion(editor)
    }
}