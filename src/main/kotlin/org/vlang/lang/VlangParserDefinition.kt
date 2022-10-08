package org.vlang.lang

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.ParserDefinition.SpaceRequirements
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import org.vlang.lang.VlangTypes.Factory
import org.vlang.lang.lexer.VlangLexer
import org.vlang.lang.psi.VlangDocTokenTypes
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.VlangTokenTypes
import org.vlang.lang.psi.impl.VlangDocCommentImpl

class VlangParserDefinition : ParserDefinition {
    override fun createLexer(project: Project) = VlangLexer()

    override fun createParser(project: Project) = VlangParser()

    override fun getWhitespaceTokens() = VlangTokenTypes.WHITE_SPACES

    override fun getCommentTokens() = VlangTokenTypes.COMMENTS

    override fun getStringLiteralElements() = VlangTokenTypes.STRING_LITERALS

    override fun getFileNodeType() = VlangFileElementType.INSTANCE

    override fun createFile(viewProvider: FileViewProvider) = VlangFile(viewProvider)

    override fun spaceExistenceTypeBetweenTokens(left: ASTNode, right: ASTNode) = SpaceRequirements.MAY

    override fun createElement(node: ASTNode): PsiElement {
        if (node.elementType == VlangDocTokenTypes.DOC_COMMENT) {
            return VlangDocCommentImpl(node)
        }
        return Factory.createElement(node)
    }

    companion object {
        val typeCastFunctions = setOf(
            "bool",
            "string",

            "int",
            "i8",
            "i16",
            "i32",
            "i64",
            "i128",

            "u8",
            "u16",
            "u32",
            "u64",
            "u128",

            "rune",
            "byte",

            "f32",
            "f64",

            "isize",
            "usize",

            "voidptr",

            "any",
        )
    }
}
