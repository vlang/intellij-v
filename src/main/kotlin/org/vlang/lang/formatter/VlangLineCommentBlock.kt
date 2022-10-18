package org.vlang.lang.formatter

import com.intellij.formatting.Block
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.formatter.common.AbstractBlock

class VlangLineCommentBlock(node: ASTNode) : AbstractBlock(node, null, null) {
    private var subBlocks: MutableList<Block>? = null

    override fun buildChildren(): List<VlangLineCommentBlock> = emptyList()

    override fun getSubBlocks(): MutableList<Block> {
        if (subBlocks == null) {
            subBlocks = buildSubBlocks()
        }
        return subBlocks ?: mutableListOf()
    }

    override fun getSpacing(child1: Block?, child2: Block) = null

    private fun buildSubBlocks(): MutableList<Block> {
        val result = mutableListOf<Block>()
        val range = textRange
        val node = node
        val text = node.text
        result.add(SlashesBlock(node, TextRange.from(range.startOffset, 2)))
        val spaceCount = StringUtil.countChars(text, ' ', 2, true)
        result.add(CommentTextBlock(node, TextRange.create(range.startOffset + 2 + spaceCount, range.endOffset)))
        return result
    }

    override fun isLeaf() = false

    private class SlashesBlock(node: ASTNode, private val textRange: TextRange) : AbstractBlock(node, null, null) {
        override fun buildChildren(): List<Block> = emptyList()

        override fun getSpacing(child1: Block?, child2: Block) = null

        override fun isLeaf() = true

        override fun getTextRange() = textRange

        override fun toString(): String {
            val start = myNode.textRange.startOffset
            return myNode.chars.subSequence(textRange.startOffset - start, textRange.endOffset - start).toString()
        }
    }

    private class CommentTextBlock(node: ASTNode, private val textRange: TextRange) : AbstractBlock(node, null, null) {
        override fun buildChildren(): List<Block> = emptyList()

        override fun getSpacing(child1: Block?, child2: Block) = null

        override fun isLeaf() = true

        override fun getTextRange() = textRange

        fun getText(): String {
            val start = myNode.textRange.startOffset
            return myNode.chars.subSequence(textRange.startOffset - start, textRange.endOffset - start).toString()
        }

        override fun toString(): String {
            return getText()
        }
    }
}
