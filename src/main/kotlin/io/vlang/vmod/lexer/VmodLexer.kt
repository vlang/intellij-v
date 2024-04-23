package io.vlang.vmod.lexer

import com.intellij.lexer.FlexAdapter
import com.intellij.lexer.MergingLexerAdapter
import com.intellij.psi.tree.TokenSet
import io.vlang.lang.vmod._VmodLexer
import io.vlang.vmod.psi.VmodTokenTypes

class VmodLexer : MergingLexerAdapter(
    FlexAdapter(_VmodLexer()),
    TokenSet.orSet(VmodTokenTypes.COMMENTS, VmodTokenTypes.WHITE_SPACES)
)
