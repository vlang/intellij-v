package org.vlang.lang.psi

import com.intellij.psi.TokenType
import com.intellij.psi.tree.TokenSet
import org.vlang.lang.VlangTypes.*

object VlangTokenTypes {
    @JvmField val LINE_COMMENT = VlangTokenType("VLANG_LINE_COMMENT")
    @JvmField val HASH_COMMENT = VlangTokenType("VLANG_HASH_COMMENT")
    @JvmField val COMPILE_DIRECTIVE = VlangTokenType("VLANG_COMPILE_DIRECTIVE")
    @JvmField val MULTI_LINE_COMMENT = VlangTokenType("VLANG_MULTI_LINE_COMMENT")

    @JvmField val WS = VlangTokenType("VLANG_WHITESPACE")
    @JvmField val NLS = VlangTokenType("VLANG_WS_NEW_LINES")

    val IDENTIFIERS = TokenSet.create(IDENTIFIER)
    val COMMENTS = TokenSet.create(LINE_COMMENT, MULTI_LINE_COMMENT, HASH_COMMENT, COMPILE_DIRECTIVE, VlangDocElementTypes.DOC_COMMENT)
    val STRING_LITERALS = TokenSet.create(
        RAW_STRING,
        CHAR,
        SINGLE_QUOTE,
        DOUBLE_QUOTE,
        BACKTICK,
        OPEN_QUOTE,
        CLOSING_QUOTE,
        LITERAL_STRING_TEMPLATE_ENTRY,
    )
    val NUMBERS = TokenSet.create(
        INT,
        FLOAT,
        HEX,
        OCT,
        BIN
    )
    val BOOL_LITERALS = TokenSet.create(TRUE, FALSE)

    val KEYWORDS = TokenSet.create(
        BREAK,
        CASE,
        CONST,
        CONTINUE,
        STATIC,
        DEFER,
        ELSE,
        FOR,
        FN,
        GO,
        GOTO,
        IF,
        IMPORT,
        INTERFACE,
        MODULE,
        IN,
        NOT_IN,
        RETURN,
        SELECT,
        STRUCT,
        TYPE_,
        PUB,
        AS,
        MUT,
        IF_COMPILE_TIME,
        ELSE_COMPILE_TIME,
        UNSAFE,
        BUILTIN_GLOBAL,
        ASSERT,
        ENUM,
        MATCH,
        OR,
        IS,
        NOT_IS,
        FOR_COMPILE_TIME,
        UNION,
        LOCK,
        RLOCK,
        SHARED,
        ASM,
        VOLATILE,
        NIL,
        DUMP,
        TYPEOF,
        OFFSETOF,
        SIZEOF,
        ISREFTYPE,
        SPAWN,
        NONE,
        ATOMIC,
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
        VAR_ASSIGN,
        TILDA,
    )

    val STRING_INTERPOLATION = TokenSet.create(
        LONG_TEMPLATE_ENTRY_START,
        SHORT_STRING_TEMPLATE_ENTRY,
        TEMPLATE_ENTRY_END,
        TEMPLATE_ENTRY_START,
    )

    val WHITE_SPACES = TokenSet.create(WS, NLS, TokenType.WHITE_SPACE)
}
