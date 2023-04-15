package org.vlang.lang

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.ParserDefinition.SpaceRequirements
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.vlang.lang.VlangTypes.Factory
import org.vlang.lang.doc.psi.impl.VlangDocCommentImpl
import org.vlang.lang.lexer.VlangLexer
import org.vlang.lang.psi.VlangDocElementTypes
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.VlangTokenTypes

class VlangParserDefinition : ParserDefinition {
    override fun createLexer(project: Project) = VlangLexer()

    override fun createParser(project: Project) = VlangParser()

    override fun getWhitespaceTokens() = VlangTokenTypes.WHITE_SPACES

    override fun getCommentTokens() = VlangTokenTypes.COMMENTS

    override fun getStringLiteralElements() = VlangTokenTypes.STRING_LITERALS

    override fun getFileNodeType() = VlangFileElementType.INSTANCE

    override fun createFile(viewProvider: FileViewProvider): PsiFile = VlangFile(viewProvider)

    override fun spaceExistenceTypeBetweenTokens(left: ASTNode, right: ASTNode) = SpaceRequirements.MAY

    override fun createElement(node: ASTNode): PsiElement {
        if (node.elementType == VlangDocElementTypes.DOC_COMMENT) {
            return VlangDocCommentImpl(node.elementType, node.text)
        }
        return Factory.createElement(node)
    }
}
