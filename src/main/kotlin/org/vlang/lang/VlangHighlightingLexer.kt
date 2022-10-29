package org.vlang.lang

import com.intellij.lexer.LayeredLexer
import org.vlang.lang.lexer.VlangLexer

class VlangHighlightingLexer : LayeredLexer(VlangLexer()) {
    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        val end = if (endOffset > buffer.length) {
            buffer.length
        } else {
            endOffset
        }
        super.start(buffer, 0, end, initialState)
    }
}
