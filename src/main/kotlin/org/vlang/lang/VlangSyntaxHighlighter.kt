package org.vlang.lang

import com.intellij.ide.highlighter.JavaHighlightingColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import org.vlang.lang.lexer.VlangLexer

class VlangSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer() = VlangLexer()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        val attr = when {
            VlangParserDefinition.COMMENTS.contains(tokenType) -> JavaHighlightingColors.LINE_COMMENT
            VlangParserDefinition.STRING_LITERALS.contains(tokenType) -> JavaHighlightingColors.STRING
            VlangParserDefinition.NUMBERS.contains(tokenType) -> JavaHighlightingColors.NUMBER
            VlangParserDefinition.KEYWORDS.contains(tokenType) -> JavaHighlightingColors.KEYWORD
            VlangParserDefinition.OPERATORS.contains(tokenType) -> JavaHighlightingColors.OPERATION_SIGN
            else -> null
        }

        return if (attr == null) emptyArray() else arrayOf(attr)
    }
}
