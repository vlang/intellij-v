package org.vlang.lang

import com.intellij.lexer.LayeredLexer
import com.intellij.psi.tree.IElementType
import org.vlang.lang.lexer.VlangLexer

class VlangHighlightingLexer : LayeredLexer(VlangLexer()) {
    init {
        registerSelfStoppingLayer(
            VlangLexer(),
            arrayOf(VlangTypes.OPEN_QUOTE),
            IElementType.EMPTY_ARRAY
        )
    }
}
