package org.vlang.lang

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType

class VlangBraceMatcher : PairedBraceMatcher {
    override fun getPairs() = arrayOf(
        BracePair(VlangTypes.LBRACE, VlangTypes.RBRACE, true),
        BracePair(VlangTypes.LBRACK, VlangTypes.RBRACK, true),
        BracePair(VlangTypes.LPAREN, VlangTypes.RPAREN, true)
    )

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?) = true

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int) = openingBraceOffset
}
