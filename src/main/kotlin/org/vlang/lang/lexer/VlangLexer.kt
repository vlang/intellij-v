package org.vlang.lang.lexer

import com.intellij.lexer.FlexAdapter
import com.intellij.lexer.LexerPosition
import com.intellij.lexer.MergingLexerAdapter
import com.intellij.psi.tree.TokenSet
import org.vlang.lang._VlangLexer
import org.vlang.lang.psi.VlangTokenTypes

class VlangLexer : MergingLexerAdapter(
    FlexAdapter(_VlangLexer()),
    TokenSet.orSet(VlangTokenTypes.COMMENTS, VlangTokenTypes.WHITE_SPACES)) {

    override fun getState(): Int {
        myDelegate.tokenType

        return super.getState()
    }

    override fun restore(position: LexerPosition) {
        super.restore(position)
    }
}
