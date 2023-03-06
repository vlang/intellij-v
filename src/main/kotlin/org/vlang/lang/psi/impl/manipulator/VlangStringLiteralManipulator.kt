package org.vlang.lang.psi.impl.manipulator

import com.intellij.openapi.util.TextRange
import com.intellij.psi.ElementManipulator
import org.vlang.lang.psi.impl.VlangStringLiteralImpl

class VlangStringLiteralManipulator : ElementManipulator<VlangStringLiteralImpl> {
    override fun handleContentChange(literal: VlangStringLiteralImpl, range: TextRange, newContent: String?): VlangStringLiteralImpl {
        val newText = range.replace(literal.text, newContent!!)
        return literal.updateText(newText) as VlangStringLiteralImpl
    }

    override fun handleContentChange(element: VlangStringLiteralImpl, newContent: String?): VlangStringLiteralImpl {
        return handleContentChange(element, TextRange(0, element.textLength), newContent)
    }

    override fun getRangeInElement(element: VlangStringLiteralImpl): TextRange {
        if (element.textLength == 0) {
            return TextRange.EMPTY_RANGE
        }

        val s = element.text
        val quote = s[0]
        val startOffset = if (isQuote(quote)) 1 else 2
        var endOffset = s.length
        if (s.length > 1) {
            val lastChar = s[s.length - 1]
            if (isQuote(quote) && lastChar == quote) {
                endOffset = s.length - 1
            }
            if (!isQuote(quote) && isQuote(lastChar)) {
                endOffset = s.length - 1
            }
        }
        return TextRange.create(startOffset, endOffset)
    }

    private fun isQuote(ch: Char) = ch == '"' || ch == '\'' || ch == '`'
}
