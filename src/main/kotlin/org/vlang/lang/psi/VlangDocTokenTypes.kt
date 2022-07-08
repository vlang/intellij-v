package org.vlang.lang.psi

import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiBuilderFactory
import com.intellij.psi.tree.ILazyParseableElementType
import org.vlang.lang.VlangLanguage
import org.vlang.lang.lexer.VlangDocLexer

object VlangDocTokenTypes {
    @JvmField
    val DOC_COMMENT_START = VlangTokenType("DOC_COMMENT_START")

    @JvmField
    val DOC_COMMENT_BODY = VlangTokenType("DOC_COMMENT_BODY")

    @JvmField
    val DOC_COMMENT_TAG = VlangTokenType("DOC_COMMENT_TAG")

    @JvmField
    val DOC_COMMENT_LEADING_ASTERISK = VlangTokenType("DOC_COMMENT_LEADING_ASTERISK")

    @JvmField
    val DOC_COMMENT_END = VlangTokenType("DOC_COMMENT_END")

    @JvmField
    val DOC_COMMENT = VlangDocCommentElementType()

    class VlangDocCommentElementType : ILazyParseableElementType("VLANG_DOC_COMMENT", VlangLanguage.INSTANCE) {
        override fun parseContents(chameleon: ASTNode): ASTNode {
            val builder = PsiBuilderFactory.getInstance().createBuilder(
                chameleon.treeParent.psi.project,
                chameleon,
                VlangDocLexer(),
                language,
                chameleon.chars
            )
            doParse(builder)
            return builder.treeBuilt.firstChildNode
        }

        private fun doParse(builder: PsiBuilder) {
            val root = builder.mark()
            while (!builder.eof()) {
                builder.advanceLexer()
            }
            root.done(this)
        }
    }
}
