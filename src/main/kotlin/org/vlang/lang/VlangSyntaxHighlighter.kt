package org.vlang.lang

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import org.vlang.ide.colors.VlangColor
import org.vlang.lang.VlangTypes.*
import org.vlang.lang.psi.VlangDocElementTypes.DOC_COMMENT
import org.vlang.lang.psi.VlangTokenTypes.BOOL_LITERALS
import org.vlang.lang.psi.VlangTokenTypes.COMMENTS
import org.vlang.lang.psi.VlangTokenTypes.COMPILE_DIRECTIVE
import org.vlang.lang.psi.VlangTokenTypes.KEYWORDS
import org.vlang.lang.psi.VlangTokenTypes.NUMBERS
import org.vlang.lang.psi.VlangTokenTypes.OPERATORS
import org.vlang.lang.psi.VlangTokenTypes.STRING_LITERALS

class VlangSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer() = VlangHighlightingLexer()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> =
        pack(map(tokenType)?.textAttributesKey)

    companion object {
        fun map(tokenType: IElementType): VlangColor? = when (tokenType) {
            LITERAL_STRING_TEMPLATE_ESCAPE_ENTRY -> VlangColor.VALID_STRING_ESCAPE
            DOC_COMMENT                          -> VlangColor.LINE_COMMENT
            COMPILE_DIRECTIVE                    -> VlangColor.COMPILE_DIRECTIVE

            LPAREN, RPAREN                       -> VlangColor.PARENTHESES
            LBRACE, RBRACE                       -> VlangColor.BRACES
            LBRACK, RBRACK                       -> VlangColor.BRACKETS

            DOT                                  -> VlangColor.DOT
            COMMA                                -> VlangColor.COMMA

            SHORT_TEMPLATE_ENTRY_START           -> VlangColor.STRING_INTERPOLATION
            LONG_TEMPLATE_ENTRY_START            -> VlangColor.STRING_INTERPOLATION
            TEMPLATE_ENTRY_END                   -> VlangColor.STRING_INTERPOLATION

            in KEYWORDS                          -> VlangColor.KEYWORD
            in BOOL_LITERALS                     -> VlangColor.KEYWORD
            in STRING_LITERALS                   -> VlangColor.STRING
            in NUMBERS                           -> VlangColor.NUMBER
            in OPERATORS                         -> VlangColor.OPERATOR
            in COMMENTS                          -> VlangColor.LINE_COMMENT

            else                                 -> null
        }
    }
}
