package org.vlang.lang.lexer

import com.intellij.lexer.FlexAdapter
import com.intellij.lexer.MergingLexerAdapter
import com.intellij.psi.tree.TokenSet
import org.vlang.lang._VlangDocLexer
import org.vlang.lang.psi.VlangDocTokenTypes

class VlangDocLexer :
    MergingLexerAdapter(FlexAdapter(_VlangDocLexer()), TokenSet.create(VlangDocTokenTypes.DOC_COMMENT_BODY))
