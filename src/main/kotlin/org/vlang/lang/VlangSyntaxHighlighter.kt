package org.vlang.lang

import com.intellij.ide.highlighter.JavaHighlightingColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import org.vlang.lang.lexer.VlangLexer
import org.vlang.lang.psi.VlangDocTokenTypes
import org.vlang.lang.psi.VlangTokenTypes

class VlangSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer() = VlangLexer()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        val attr = when {
            tokenType == VlangTypes.LANGUAGE_INJECTION -> JavaHighlightingColors.LINE_COMMENT
            tokenType == VlangDocTokenTypes.DOC_COMMENT -> JavaHighlightingColors.DOC_COMMENT

            VlangTokenTypes.STRING_LITERALS.contains(tokenType) -> JavaHighlightingColors.STRING
            VlangTokenTypes.NUMBERS.contains(tokenType) -> JavaHighlightingColors.NUMBER
            VlangTokenTypes.KEYWORDS.contains(tokenType) -> JavaHighlightingColors.KEYWORD
            VlangTokenTypes.OPERATORS.contains(tokenType) -> JavaHighlightingColors.OPERATION_SIGN
            VlangTokenTypes.COMMENTS.contains(tokenType) -> JavaHighlightingColors.LINE_COMMENT
            else -> null
        }

        return if (attr == null) emptyArray() else arrayOf(attr)
    }
}
