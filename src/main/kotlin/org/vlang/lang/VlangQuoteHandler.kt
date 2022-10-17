package org.vlang.lang

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler
import com.intellij.openapi.editor.highlighter.HighlighterIterator

class VlangQuoteHandler : SimpleTokenSetQuoteHandler(
    VlangTypes.STRING_TEMPLATE,
    VlangTypes.OPEN_QUOTE,
    VlangTypes.CLOSING_QUOTE,
    VlangTypes.CHAR,
    VlangTypes.BACKTICK,
) {
    override fun isClosingQuote(iterator: HighlighterIterator, offset: Int): Boolean {
        if (iterator.tokenType == VlangTypes.CHAR) {
            return super.isClosingQuote(iterator, offset)
        }
        return iterator.tokenType == VlangTypes.CLOSING_QUOTE
    }

    override fun isOpeningQuote(iterator: HighlighterIterator, offset: Int): Boolean {
        if (iterator.tokenType == VlangTypes.CHAR) {
            return super.isOpeningQuote(iterator, offset)
        }
        return iterator.tokenType == VlangTypes.OPEN_QUOTE
    }

    override fun isNonClosedLiteral(iterator: HighlighterIterator?, chars: CharSequence?): Boolean {
        return super.isNonClosedLiteral(iterator, chars)
    }
}
