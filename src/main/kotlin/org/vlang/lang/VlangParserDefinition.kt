package org.vlang.lang

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.ParserDefinition.SpaceRequirements
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import org.vlang.lang.VlangTypes.*
import org.vlang.lang.lexer.VlangLexer
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.VlangTokenType

class VlangParserDefinition : ParserDefinition {
    override fun createLexer(project: Project) = VlangLexer()

    override fun getWhitespaceTokens() = WHITE_SPACES

    override fun getCommentTokens(): TokenSet = COMMENTS

    override fun getStringLiteralElements(): TokenSet = STRING_LITERALS

    override fun createParser(project: Project) = VlangParser()

    override fun getFileNodeType() = FILE

    override fun createFile(viewProvider: FileViewProvider) = VlangFile(viewProvider)

    override fun spaceExistenceTypeBetweenTokens(left: ASTNode, right: ASTNode) = SpaceRequirements.MAY

    override fun createElement(node: ASTNode): PsiElement = Factory.createElement(node)
    
    companion object {
        @JvmField
        val LINE_COMMENT: IElementType = VlangTokenType("GO_LINE_COMMENT")
        @JvmField
        val MULTILINE_COMMENT: IElementType = VlangTokenType("GO_MULTILINE_COMMENT")

        @JvmField
        val WS: IElementType = VlangTokenType("GO_WHITESPACE")
        @JvmField
        val NLS: IElementType = VlangTokenType("GO_WS_NEW_LINES")

        val COMMENTS = TokenSet.create(LINE_COMMENT, MULTILINE_COMMENT)
        val STRING_LITERALS = TokenSet.create(STRING, RAW_STRING, CHAR)
        val NUMBERS = TokenSet.create(INT, FLOAT, FLOATI, DECIMALI, FLOATI) // todo: HEX, OCT,

        val KEYWORDS = TokenSet.create(
            BREAK, CASE, /*CHAN*/ CONST, CONTINUE, DEFAULT, DEFER, ELSE, FALLTHROUGH, FOR, FN, GO, GOTO, IF, IMPORT,
            INTERFACE, MAP, MODULE, IN, RETURN, SELECT, STRUCT, SWITCH, TYPE_, PUB, AS, MUT, IF_COMPILE_TIME, ELSE_COMPILE_TIME,
            UNSAFE
        )
        val OPERATORS = TokenSet.create(
            EQ,
            ASSIGN,
            NOT_EQ,
            NOT,
            PLUS_PLUS,
            PLUS_ASSIGN,
            PLUS,
            MINUS_MINUS,
            MINUS_ASSIGN,
            MINUS,
            COND_OR,
            BIT_OR_ASSIGN,
            BIT_OR,
            BIT_CLEAR_ASSIGN,
            BIT_CLEAR,
            COND_AND,
            BIT_AND_ASSIGN,
            BIT_AND,
            SHIFT_LEFT_ASSIGN,
            SHIFT_LEFT,
            SEND_CHANNEL,
            LESS_OR_EQUAL,
            LESS,
            BIT_XOR_ASSIGN,
            BIT_XOR,
            MUL_ASSIGN,
            MUL,
            QUOTIENT_ASSIGN,
            QUOTIENT,
            REMAINDER_ASSIGN,
            REMAINDER,
            SHIFT_RIGHT_ASSIGN,
            SHIFT_RIGHT,
            GREATER_OR_EQUAL,
            GREATER,
            VAR_ASSIGN
        )

        val WHITE_SPACES = TokenSet.create(WS, NLS)
        val FILE = IFileElementType(VlangLanguage.INSTANCE)
    }
}
