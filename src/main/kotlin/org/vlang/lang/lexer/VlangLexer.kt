package org.vlang.lang.lexer

import com.intellij.lexer.FlexAdapter
import com.intellij.lexer.MergingLexerAdapter
import com.intellij.psi.tree.TokenSet
import org.vlang.lang.VlangParserDefinition
import org.vlang.lang._VlangLexer

class VlangLexer : MergingLexerAdapter(
    FlexAdapter(_VlangLexer()),
    TokenSet.orSet(VlangParserDefinition.COMMENTS, VlangParserDefinition.WHITE_SPACES)
)
