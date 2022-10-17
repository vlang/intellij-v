package org.vlang.lang

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import org.vlang.lang.psi.VlangTokenTypes

class VlangBraceMatcher : PairedBraceMatcher {
    override fun getPairs() = arrayOf(
        BracePair(VlangTypes.LBRACE, VlangTypes.RBRACE, true),
        BracePair(VlangTypes.LBRACK, VlangTypes.RBRACK, false),
        BracePair(VlangTypes.LPAREN, VlangTypes.RPAREN, false),
    )

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, type: IElementType?) =
        VlangTokenTypes.COMMENTS.contains(type) ||
                VlangTokenTypes.WHITE_SPACES.contains(type) ||
                type === VlangTypes.SEMICOLON ||
                type === VlangTypes.COMMA ||
                type === VlangTypes.RPAREN ||
                type === VlangTypes.RBRACK ||
                type === VlangTypes.RBRACE ||
                type === VlangTypes.LBRACE

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int) = openingBraceOffset
}
