package org.vlang.ide.colors

import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors as Default

@Suppress("UnstableApiUsage")
enum class VlangColor(readableName: @NlsContexts.AttributeDescriptor String, default: TextAttributesKey? = null) {
    KEYWORD("Keywords//Keyword", Default.KEYWORD),

    // Declarations
    FUNCTION("Functions//Module local function/method", Default.FUNCTION_DECLARATION),
    PUBLIC_FUNCTION("Functions//Public function/method", Default.FUNCTION_DECLARATION),
    STRUCT("Types//Module local struct", Default.CLASS_NAME),
    PUBLIC_STRUCT("Types//Public struct", Default.CLASS_NAME),
    UNION("Types//Module local union", Default.CLASS_NAME),
    PUBLIC_UNION("Types//Public union", Default.CLASS_NAME),
    ENUM("Types//Module local enum", Default.CLASS_NAME),
    PUBLIC_ENUM("Types//Public enum", Default.CLASS_NAME),
    INTERFACE("Types//Module local interface", Default.INTERFACE_NAME),
    PUBLIC_INTERFACE("Types//Public interface", Default.INTERFACE_NAME),
    MODULE("Declarations//Module", Default.IDENTIFIER),

    // Interface/Struct/Enum fields
    FIELD("Fields//Module local field", Default.INSTANCE_FIELD),
    MUTABLE_FIELD("Fields//Mutable field", Default.INSTANCE_FIELD),
    PUBLIC_FIELD("Fields//Public field", Default.INSTANCE_FIELD),
    PUBLIC_MUTABLE_FIELD("Fields//Public mutable field", Default.INSTANCE_FIELD),
    ENUM_FIELD("Fields//Enum field", Default.INSTANCE_FIELD),

    // Interface methods
    INTERFACE_METHOD("Interface Methods//Module local method", Default.INSTANCE_METHOD),

    CONSTANT("Constants//Module local constant", Default.CONSTANT),
    PUBLIC_CONSTANT("Constants//Public constant", Default.CONSTANT),

    TYPE_ALIAS("Type aliases//Module local type alias", Default.IDENTIFIER),
    PUBLIC_TYPE_ALIAS("Type aliases//Public type alias", Default.IDENTIFIER),

    // Variable like
    VARIABLE("Variables//Variable", Default.LOCAL_VARIABLE),
    MUTABLE_VARIABLE("Variables//Mutable variable", Default.LOCAL_VARIABLE),
    GLOBAL_VARIABLE("Variables//Global variable", Default.GLOBAL_VARIABLE),
    CAPTURED_VARIABLE("Variables//Captured variable", Default.LOCAL_VARIABLE),
    MUTABLE_CAPTURED_VARIABLE("Variables//Mutable captured variable", Default.LOCAL_VARIABLE),
    PARAMETER("Variables//Parameter", Default.PARAMETER),
    MUTABLE_PARAMETER("Variables//Mutable parameter", Default.PARAMETER),
    RECEIVER("Variables//Receiver", Default.PARAMETER),
    MUTABLE_RECEIVER("Variables//Mutable receiver", Default.PARAMETER),

    // Types
    BUILTIN_TYPE("Types//Builtin type", Default.KEYWORD),

    // Labels
    LABEL("Labels//Label", Default.LABEL),
    USED_LABEL("Labels//Used label", Default.LABEL),

    // Comments
    LINE_COMMENT("Comments//Line comments", Default.LINE_COMMENT),
    BLOCK_COMMENT("Comments//Block comments", Default.BLOCK_COMMENT),

    // Literals
    NUMBER("Literals//Number", Default.NUMBER),
    CHAR("Literals//Character", Default.STRING),

    // Strings
    STRING("Literals//Strings//String literals", Default.STRING),
    RAW_STRING("Literals//Strings//Raw string literals", Default.STRING),
    C_STRING("Literals//Strings//C string literals", Default.STRING),
    VALID_STRING_ESCAPE("Literals//Strings//Valid string escape", Default.VALID_STRING_ESCAPE),
    // String interpolation
    STRING_INTERPOLATION("Literals//Strings//String interpolation", Default.VALID_STRING_ESCAPE),
    // Literals END

    // Braces and operators
    BRACES("Braces and Operators//Braces", Default.BRACES),
    BRACKETS("Braces and Operators//Brackets", Default.BRACKETS),
    OPERATOR("Braces and Operators//Operators", Default.OPERATION_SIGN),
    DOT("Braces and Operators//Dot", Default.DOT),
    COMMA("Braces and Operators//Comma", Default.COMMA),
    PARENTHESES("Braces and Operators//Parentheses", Default.PARENTHESES),

    // Compile time
    CT_CONSTANT("Compile time//Constant", Default.IDENTIFIER),
    CT_FIELD("Compile time//Field", Default.IDENTIFIER),
    CT_METHOD_CALL("Compile time//Method call", Default.IDENTIFIER),

    // SQL
    SQL_KEYWORD("SQL//Keyword", Default.KEYWORD),
    SQL_CODE("SQL//Code"),

    // Attributes
    ATTRIBUTE("Attributes//Attribute", Default.METADATA),

    // Unsafe
    UNSAFE_CODE("Unsafe//Unsafe code"),

    // Generics
    GENERIC_PARAMETER("Generics//Generic parameter", Default.IDENTIFIER),

    // Docs
    DOC_COMMENT("Doc//Comment", Default.DOC_COMMENT),
    DOC_HEADING("Doc//Heading", Default.DOC_COMMENT_TAG),
    DOC_LINK("Doc//Link", Default.DOC_COMMENT_TAG_VALUE),
    DOC_EMPHASIS("Doc//Italic"),
    DOC_STRONG("Doc//Bold"),
    DOC_CODE("Doc//Code", Default.DOC_COMMENT_MARKUP),
    COMMENT_REFERENCE("Doc//Comment reference", Default.DOC_COMMENT),

    // C Interop
    COMPILE_DIRECTIVE("C Interop//Compile directive", Default.METADATA),
    ;

    val textAttributesKey = TextAttributesKey.createTextAttributesKey("org.vlang.$name", default)
    val attributesDescriptor = AttributesDescriptor(readableName, textAttributesKey)
    val testSeverity: HighlightSeverity = HighlightSeverity(name, HighlightSeverity.INFORMATION.myVal)
}
