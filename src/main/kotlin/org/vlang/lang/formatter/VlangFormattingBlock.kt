package org.vlang.lang.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiComment
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock
import com.intellij.psi.impl.source.tree.LeafPsiElement
import org.vlang.lang.psi.*

class VlangFormattingBlock(
    node: ASTNode,
    wrap: Wrap? = Wrap.createWrap(WrapType.NONE, false),
    alignment: Alignment? = null,
    private val withIdent: Boolean = false,
    private val spacingBuilder: SpacingBuilder,
) : AbstractBlock(node, wrap, alignment) {

    override fun buildChildren(): List<AbstractBlock> {
        val blocks = mutableListOf<AbstractBlock>()
        val parent = node.psi ?: return emptyList()

        var child = myNode.firstChildNode
        while (child != null) {
            if (child.elementType == TokenType.WHITE_SPACE) {
                child = child.treeNext
                continue
            }

            val needIdent = when (parent) {
                is VlangBlock                     -> true
                is VlangInterfaceType             -> true
                is VlangStructType                -> true
                is VlangAnonymousStructType       -> true
                is VlangEnumType                  -> true
                is VlangConstDeclaration          -> true
                is VlangLiteralValueExpression    -> true
                is VlangMatchArms                 -> true
                is VlangSelectArms                -> true
                is VlangMapInitExpr               -> true
                is VlangGlobalVariableDeclaration -> true
                is VlangArrayCreation             -> true
                is VlangSqlBlock                  -> true
                is VlangAsmBlock                  -> true
                is VlangArgumentList              -> true
                else                              -> false
            } && (child !is LeafPsiElement || child is PsiComment)

            val block = VlangFormattingBlock(child, spacingBuilder = spacingBuilder, withIdent = needIdent)
            blocks.add(block)

            child = child.treeNext
        }

        return blocks
    }

    override fun getSpacing(child1: Block?, child2: Block) = spacingBuilder.getSpacing(this, child1, child2)

    override fun getIndent(): Indent? = if (withIdent)
        Indent.getNormalIndent(false)
    else
        Indent.getNoneIndent()

    override fun isLeaf() = node.firstChildNode == null
}
