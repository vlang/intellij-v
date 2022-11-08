package org.vlang.vmod

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import org.vlang.ide.colors.VlangColor
import org.vlang.vmod.lexer.VmodLexer
import org.vlang.vmod.psi.VmodTokenTypes

class VmodSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer() = VmodLexer()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> =
        pack(map(tokenType)?.textAttributesKey)

    companion object {
        fun map(tokenType: IElementType): VlangColor? = when (tokenType) {
            in VmodTokenTypes.COMMENTS        -> VlangColor.LINE_COMMENT
            in VmodTokenTypes.KEYWORDS        -> VlangColor.KEYWORD
            in VmodTokenTypes.STRING_LITERALS -> VlangColor.STRING
            else                              -> null
        }
    }
}
