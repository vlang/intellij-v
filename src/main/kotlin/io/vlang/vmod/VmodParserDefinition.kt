package io.vlang.vmod

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.ParserDefinition.SpaceRequirements
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import io.vlang.lang.vmod.VmodParser
import io.vlang.lang.vmod.VmodTypes
import io.vlang.vmod.lexer.VmodLexer
import io.vlang.vmod.psi.VmodFile
import io.vlang.vmod.psi.VmodTokenTypes

class VmodParserDefinition : ParserDefinition {
    override fun createLexer(project: Project) = VmodLexer()

    override fun createParser(project: Project) = VmodParser()

    override fun getWhitespaceTokens() = VmodTokenTypes.WHITE_SPACES

    override fun getCommentTokens() = VmodTokenTypes.COMMENTS

    override fun getStringLiteralElements() = VmodTokenTypes.STRING_LITERALS

    override fun getFileNodeType() = VmodFileElementType.INSTANCE

    override fun createFile(viewProvider: FileViewProvider) = VmodFile(viewProvider)

    override fun spaceExistenceTypeBetweenTokens(left: ASTNode, right: ASTNode) = SpaceRequirements.MAY

    override fun createElement(node: ASTNode): PsiElement = VmodTypes.Factory.createElement(node)
}
