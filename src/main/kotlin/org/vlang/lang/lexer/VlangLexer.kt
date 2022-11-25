package org.vlang.lang.lexer

import com.intellij.lexer.FlexAdapter
import com.intellij.lexer.Lexer
import com.intellij.lexer.MergeFunction
import com.intellij.lexer.MergingLexerAdapterBase
import com.intellij.psi.tree.IElementType
import org.vlang.lang._VlangLexer
import org.vlang.lang.psi.VlangTokenTypes.MULTI_LINE_COMMENT
import org.vlang.lang.psi.VlangTokenTypes.MULTI_LINE_COMMENT_END
import org.vlang.lang.psi.VlangTokenTypes.MULTI_LINE_COMMENT_START

class VlangLexer : MergingLexerAdapterBase(createLexer()) {
    override fun getMergeFunction() = MERGE_FUNCTION

    companion object {
        private val MERGE_FUNCTION = MergeFunction { firstTokenType: IElementType, originalLexer: Lexer ->
            if (firstTokenType == MULTI_LINE_COMMENT_START) {
                // merge multiline comments that are parsed in parts into single element
                while (true) {
                    val nextTokenType = originalLexer.tokenType ?: break
                    // EOF reached, multi-line comment is not closed
                    originalLexer.advance()
                    if (nextTokenType == MULTI_LINE_COMMENT_END) break
                }
                return@MergeFunction MULTI_LINE_COMMENT
            }
//            if (firstTokenType == DOC_COMMENT_LINE) {
//                while (true) {
//                    val nextTokenType = originalLexer.tokenType ?: break
//                    if (nextTokenType != DOC_COMMENT_LINE && nextTokenType != NLS && nextTokenType != WS) {
//                        break
//                    }
//
//                    originalLexer.advance()
//                }
//                return@MergeFunction VlangDocElementTypes.DOC_COMMENT
//            }
            firstTokenType
        }

        private fun createLexer(): FlexAdapter {
            return FlexAdapter(object : _VlangLexer() {
                override fun reset(buffer: CharSequence, start: Int, end: Int, initialState: Int) {
                    val state = stack.peek() ?: initialState
                    super.reset(buffer, start, end, state)
                }
            })
        }
    }
}
