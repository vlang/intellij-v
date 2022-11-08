package org.vlang.lang;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import java.util.Stack;
import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static org.vlang.lang.psi.VlangTokenTypes.*;
import static org.vlang.lang.psi.VlangDocTokenTypes.*;

%%

%{
  protected Stack<Integer> stack = new Stack<Integer>();
  protected Stack<Integer> quotesStack = new Stack<Integer>();
  protected Stack<Integer> prevQuotesStack = new Stack<Integer>();
  protected int openBrackets = 0;
  protected int prevOpenBrackets = 0;
  private int SINGLE_QUOTE = 0;
  private int DOUBLE_QUOTE = 1;

  public void yy_push_state(int state) {
    prevOpenBrackets = openBrackets;
    openBrackets = 0;
    prevQuotesStack.addAll(quotesStack);
    quotesStack.clear();
    stack.push(state);
    yybegin(state);
  }

  public void yy_pop_state() {
    stack.pop();
    if (stack.isEmpty()) {
	  yybegin(YYINITIAL);
	} else {
	  yybegin(stack.peek());
	}
    openBrackets = prevOpenBrackets;
    prevOpenBrackets = 0;
    quotesStack.addAll(prevQuotesStack);
    prevQuotesStack.clear();
  }

  public void yy_start_string(int type) {
    quotesStack.push(type);
  }

  public void yy_end_string(int type) {
    if (quotesStack.peek() != type) {
      throw new IllegalStateException("Unmatched quotes");
    }
    quotesStack.pop();
  }

  public IElementType handle_string_end(boolean isDoubleQuote) {
    if (inside_string_with_quote(isDoubleQuote ? SINGLE_QUOTE : DOUBLE_QUOTE)) {
       return LITERAL_STRING_TEMPLATE_ENTRY;
    }

    yy_pop_state();
    yy_end_string(isDoubleQuote ? DOUBLE_QUOTE : SINGLE_QUOTE);
    yybegin(MAYBE_SEMICOLON);
    return CLOSING_QUOTE;
  }

  public boolean inside_string_with_quote(int type) {
    return !quotesStack.isEmpty() && quotesStack.peek() == type;
  }

  public boolean inside_interpolation() {
    return stack.size() > 2 && stack.get(1) == TEMPLATE_STRING;
  }

  public boolean is_interpolation_state() {
    return stack.peek() == TEMPLATE_STRING;
  }

  public _VlangLexer() {
    this((java.io.Reader)null);
    stack.push(YYINITIAL);
 }
%}

%class _VlangLexer
%implements FlexLexer, VlangTypes
%unicode
%public

%function advance
%type IElementType

NL = \n
WS = [ \t\f]

LINE_COMMENT = "//" [^\r\n]*
HASH_COMMENT = "#" [^\[] [^\r\n]*

MULTI_LINE_DEGENERATE_COMMENT = "/*" "*"+ "/"
MULTI_LINE_COMMENT_START      = "/*"
MULTI_LINE_COMMENT_END        = "*/"

LETTER = [:letter:] | "_"
DIGIT =  [:digit:]

HEX_DIGIT = [0-9A-Fa-f]
HEX_DIGIT_OR_SEP = {HEX_DIGIT} | "_"

INT_DIGIT = [0-9]
INT_DEGIT_OR_SEP = {INT_DIGIT} | "_"

OCT_DIGIT = [0-7]
OCT_DIGIT_OR_SEP = {OCT_DIGIT} | "_"

BIN_DIGIT = [0-1]
BIN_DIGIT_OR_SEP = {BIN_DIGIT} | "_"

NUM_INT = ({INT_DIGIT} {INT_DEGIT_OR_SEP}* {INT_DIGIT}) | {INT_DIGIT}
NUM_HEX = ("0x" | "0X") (({HEX_DIGIT} {HEX_DIGIT_OR_SEP}* {HEX_DIGIT}) | {HEX_DIGIT})
NUM_OCT = "0o" (({OCT_DIGIT} {OCT_DIGIT_OR_SEP}* {OCT_DIGIT}) | {OCT_DIGIT})
NUM_BIN = "0b" (({BIN_DIGIT} {BIN_DIGIT_OR_SEP}* {BIN_DIGIT}) | {BIN_DIGIT})

FLOAT_EXPONENT = [eE] [+-]? {NUM_INT}
NUM_FLOAT = (
    ({NUM_INT}? "." {NUM_INT}) {FLOAT_EXPONENT}?) |
    ({NUM_INT} {FLOAT_EXPONENT}
)

IDENT = {LETTER} {IDENT_PART}*
IDENT_PART = {LETTER} | {DIGIT}

// JS and C special identifiers like JS.function_name or C.free
SPECIAL_IDENT = ("JS." | "C.") "@"? {LETTER} ({LETTER} | {DIGIT} | "." )*

STR_DOUBLE = "\""
STR_SINGLE = "'"

// c string, like c"hello" or c'hello'
C_STR_MODIFIER = "c"

// raw string, like r"hello" or r'hello'
RAW_STR_MODIFIER = "r"

RAW_DOUBLE_QUOTE_STRING = {RAW_STR_MODIFIER} {STR_DOUBLE} [^\"]* {STR_DOUBLE}
RAW_SINGLE_QUOTE_STRING = {RAW_STR_MODIFIER} {STR_SINGLE} [^\']* {STR_SINGLE}

%state MAYBE_SEMICOLON
%state TEMPLATE_STRING
%state SHORT_TEMPLATE_ENTRY
%state SHORT_TEMPLATE_ENTRY_FIELD_NAME
%state MULTI_LINE_COMMENT_STATE
%state ASM_BLOCK
%state ASM_BLOCK_LINE

%%
<SHORT_TEMPLATE_ENTRY> {
"$"             { return SHORT_TEMPLATE_ENTRY_START; }
"${"            { yy_push_state(YYINITIAL); return LONG_TEMPLATE_ENTRY_START; }
{IDENT}         { return IDENTIFIER; }
"."             { yybegin(SHORT_TEMPLATE_ENTRY_FIELD_NAME); return DOT; }
{STR_DOUBLE}    { yybegin(TEMPLATE_STRING); return handle_string_end(true); }
{STR_SINGLE}    { yybegin(TEMPLATE_STRING); return handle_string_end(false); }
"\\" (. | "\\") { yybegin(TEMPLATE_STRING); return LITERAL_STRING_TEMPLATE_ESCAPE_ENTRY; }
\n              { yybegin(TEMPLATE_STRING); return LITERAL_STRING_TEMPLATE_ENTRY; }
.               { yybegin(TEMPLATE_STRING); return LITERAL_STRING_TEMPLATE_ENTRY; }
}

<SHORT_TEMPLATE_ENTRY_FIELD_NAME> {
{IDENT}         { return IDENTIFIER; }
"."             { return DOT; }
{STR_DOUBLE}    { yybegin(TEMPLATE_STRING); return handle_string_end(true); }
{STR_SINGLE}    { yybegin(TEMPLATE_STRING); return handle_string_end(false); }
"\\" (. | "\\") { yybegin(TEMPLATE_STRING); return LITERAL_STRING_TEMPLATE_ESCAPE_ENTRY; }
\n              { yybegin(TEMPLATE_STRING); return LITERAL_STRING_TEMPLATE_ENTRY; }
.               { yybegin(TEMPLATE_STRING); return LITERAL_STRING_TEMPLATE_ENTRY; }
}

<TEMPLATE_STRING> {
"${"          { yy_push_state(YYINITIAL);      return LONG_TEMPLATE_ENTRY_START; }
"$"           { yybegin(SHORT_TEMPLATE_ENTRY); return SHORT_TEMPLATE_ENTRY_START; }

// disabled for now
//"{" [a-zA-Z_] { yy_push_state(YYINITIAL); yypushback(1); return TEMPLATE_ENTRY_START; }
"{"           { return LITERAL_STRING_TEMPLATE_ENTRY; }

{STR_DOUBLE}  { return handle_string_end(true); }
{STR_SINGLE}  { return handle_string_end(false); }

"\\" (. | "\\") { return LITERAL_STRING_TEMPLATE_ESCAPE_ENTRY; }
[^\"\'\\{\\$]+  { return LITERAL_STRING_TEMPLATE_ENTRY; }
}

<MULTI_LINE_COMMENT_STATE> {
{MULTI_LINE_COMMENT_START}                { yy_push_state(MULTI_LINE_COMMENT_STATE); return MULTI_LINE_COMMENT_START; }
[^]                                       { return MULTI_LINE_COMMENT_BODY; }
{MULTI_LINE_COMMENT_END}                  { yy_pop_state(); return yystate() == MULTI_LINE_COMMENT_STATE
											   ? MULTI_LINE_COMMENT_BODY // inner comment closed
											   : MULTI_LINE_COMMENT_END; }
}

<YYINITIAL> {
{WS}                                      { return WS; }
{NL}+                                     { return NLS; }

{LINE_COMMENT}                            { return LINE_COMMENT; }
{HASH_COMMENT}                            { return HASH_COMMENT; }

// without this rule /*****/ is parsed as doc comment and /**/ is parsed as not closed doc comment, thanks Dart plugin
{MULTI_LINE_DEGENERATE_COMMENT}           { return MULTI_LINE_COMMENT; }
{MULTI_LINE_COMMENT_START}                { yy_push_state(MULTI_LINE_COMMENT_STATE); return MULTI_LINE_COMMENT_START; }

"`\\`"                                    { yybegin(MAYBE_SEMICOLON); return BAD_CHARACTER; }
"``"                                      { yybegin(MAYBE_SEMICOLON); return CHAR; }
"`" [^\\] "`"                             { yybegin(MAYBE_SEMICOLON); return CHAR; }
"`" \n "`"?                               { yybegin(MAYBE_SEMICOLON); return CHAR; }
"`\\" (. | "\\") "`"                      { yybegin(MAYBE_SEMICOLON); return CHAR; }

// \141`, `\342\230\205`
"`" ("\\" {OCT_DIGIT} {3}) {1,3} "`"?     { yybegin(MAYBE_SEMICOLON); return CHAR; }
"`" ("\\x" {HEX_DIGIT} {2}) {1,3} "`"?    { yybegin(MAYBE_SEMICOLON); return CHAR; }
"`\\u" {HEX_DIGIT} {4} "`"?               { yybegin(MAYBE_SEMICOLON); return CHAR; }
"`\\U" {HEX_DIGIT} {8} "`"?               { yybegin(MAYBE_SEMICOLON); return CHAR; }

{RAW_DOUBLE_QUOTE_STRING}                 { yybegin(MAYBE_SEMICOLON); return RAW_STRING; }
{RAW_SINGLE_QUOTE_STRING}                 { yybegin(MAYBE_SEMICOLON); return RAW_STRING; }

{C_STR_MODIFIER}? {STR_DOUBLE} {
    yy_push_state(TEMPLATE_STRING);
    yy_start_string(DOUBLE_QUOTE);
    return OPEN_QUOTE;
}

{C_STR_MODIFIER}? {STR_SINGLE} {
    yy_push_state(TEMPLATE_STRING);
    yy_start_string(SINGLE_QUOTE);
    return OPEN_QUOTE;
}

"..."                                     { return TRIPLE_DOT; }
".."                                      { return RANGE; }
"."                                       { return DOT; }
"~"                                       { return TILDA; }
"|"                                       { return BIT_OR; }
"{"                                       { openBrackets++; return LBRACE; }
"}" {
    if (inside_interpolation() && openBrackets == 0) {
        yy_pop_state();
        return TEMPLATE_ENTRY_END;
    }

    openBrackets--;
    yybegin(MAYBE_SEMICOLON);
    return RBRACE;
}

"#["                                      { return HASH_LBRACK; }

"["                                       { return LBRACK; }
"]"                                       { yybegin(MAYBE_SEMICOLON); return RBRACK; }

"("                                       { return LPAREN; }
")"                                       { yybegin(MAYBE_SEMICOLON); return RPAREN; }

":"                                       { yybegin(MAYBE_SEMICOLON); return COLON; }
";"                                       { return SEMICOLON; }
","                                       { return COMMA; }

"=="                                      { return EQ; }
"="                                       { return ASSIGN; }

"!="                                      { return NOT_EQ; }
"!"                                       { yybegin(MAYBE_SEMICOLON); return NOT; }
"?"                                       { yybegin(MAYBE_SEMICOLON); return QUESTION; }

"++"                                      { yybegin(MAYBE_SEMICOLON); return PLUS_PLUS; }
"+="                                      { return PLUS_ASSIGN; }
"+"                                       { return PLUS; }

"--"                                      { yybegin(MAYBE_SEMICOLON); return MINUS_MINUS; }
"-="                                      { return MINUS_ASSIGN; }
"-"                                       { return MINUS; }

"||"                                      { return COND_OR; }
"|="                                      { return BIT_OR_ASSIGN; }

"&^="                                     { return BIT_CLEAR_ASSIGN; }
"&^"                                      { return BIT_CLEAR; }
"&&"                                      { return COND_AND; }

"&="                                      { return BIT_AND_ASSIGN; }
"&"                                       { return BIT_AND; }

"<<="                                     { return SHIFT_LEFT_ASSIGN; }
"<<"                                      { return SHIFT_LEFT; }
"<-"                                      { return SEND_CHANNEL; }
"<="                                      { return LESS_OR_EQUAL; }
"<"                                       { return LESS; }

"^="                                      { return BIT_XOR_ASSIGN; }
"^"                                       { return BIT_XOR; }

"*="                                      { return MUL_ASSIGN; }
"*"                                       { return MUL; }

"/="                                      { return QUOTIENT_ASSIGN; }
"/"                                       { return QUOTIENT; }

"%="                                      { return REMAINDER_ASSIGN; }
"%"                                       { return REMAINDER; }
"@"                                       { return AT; }

">>="                                     { return SHIFT_RIGHT_ASSIGN; }
">>>="                                    { return UNSIGNED_SHIFT_RIGHT_ASSIGN; }
// ">>"                                   { return SHIFT_RIGHT; } // done in parser <<gtGt>>
// ">>>"                                  { return UNSIGNED_SHIFT_RIGHT; } // done in parser <<gtGtGt>>
">="                                      { return GREATER_OR_EQUAL; }
">"                                       { yybegin(MAYBE_SEMICOLON); return GREATER; }

"`"                                       { return BACKTICK; }
":="                                      { return VAR_ASSIGN; }
"$"                                       { return DOLLAR; }

// top level declarations
"module"                                  { return MODULE; }
"import"                                  { return IMPORT ; }
"struct"                                  { return STRUCT; }
"union"                                   { return UNION; }
"interface"                               { return INTERFACE; }
"enum"                                    { return ENUM; }
"const"                                   { return CONST; }
"type"                                    { return TYPE_; }
"fn"                                      { return FN; }

"return"                                  { yybegin(MAYBE_SEMICOLON); return RETURN; }
"select"                                  { return SELECT; }
"match"                                   { return MATCH; }
"or"                                      { return OR; }
"if"                                      { return IF; }
"else"                                    { return ELSE; }
"goto"                                    { return GOTO; }

"assert"                                  { return ASSERT; }

// loop
"for"                                     { return FOR; }
"break"                                   { yybegin(MAYBE_SEMICOLON); return BREAK; }
"continue"                                { yybegin(MAYBE_SEMICOLON); return CONTINUE; }

"unsafe"                                  { return UNSAFE; }
"defer"                                   { return DEFER; }
"go"                                      { return GO; }
"spawn"                                   { return SPAWN; }
"rlock"                                   { return RLOCK; }
"lock"                                    { return LOCK; }

"as"                                      { return AS ; }
"in"                                      { return IN; }
"is"                                      { return IS; }

// literals
"nil"                                     { yybegin(MAYBE_SEMICOLON); return NIL; }
"true"                                    { yybegin(MAYBE_SEMICOLON); return TRUE; }
"false"                                   { yybegin(MAYBE_SEMICOLON); return FALSE; }

// modifiers
"pub"                                     { return PUB; }
"mut"                                     { return MUT; }
"static"                                  { return STATIC; }
"shared"                                  { return SHARED; }
"volatile"                                { return VOLATILE; }
"__global"                                { return BUILTIN_GLOBAL; }

// builtin functions
"dump"                                    { return DUMP; }
"sizeof"                                  { return SIZEOF; }
"typeof"                                  { return TYPEOF; }
"isreftype"                               { return ISREFTYPE; }
"__offsetof"                              { return OFFSETOF; }

// compile time expressions
"$for"                                    { return FOR_COMPILE_TIME; }
"$if"                                     { return IF_COMPILE_TIME; }
"$else"                                   { return ELSE_COMPILE_TIME; }

"asm"                                     { yybegin(ASM_BLOCK); return ASM; }

{IDENT}                                   { yybegin(MAYBE_SEMICOLON); return IDENTIFIER; }
{SPECIAL_IDENT}                           { yybegin(MAYBE_SEMICOLON); return IDENTIFIER; }
"$"{IDENT}                                { yybegin(MAYBE_SEMICOLON); return IDENTIFIER; }
"@"{IDENT}                                { yybegin(MAYBE_SEMICOLON); return IDENTIFIER; }

\!in{IDENT_PART}                          { yypushback(3); yybegin(MAYBE_SEMICOLON); return NOT; }
\!is{IDENT_PART}                          { yypushback(3); yybegin(MAYBE_SEMICOLON); return NOT; }

"!in"                                     { return NOT_IN; }
"!is"                                     { return NOT_IS; }

{NUM_FLOAT}                               { yybegin(MAYBE_SEMICOLON); return FLOAT; }
{NUM_BIN}                                 { yybegin(MAYBE_SEMICOLON); return BIN; }
{NUM_OCT}                                 { yybegin(MAYBE_SEMICOLON); return OCT; }
{NUM_HEX}                                 { yybegin(MAYBE_SEMICOLON); return HEX; }
{NUM_INT}                                 { yybegin(MAYBE_SEMICOLON); return INT; }

.                                         { return BAD_CHARACTER; }
}

<MAYBE_SEMICOLON> {
{WS}                                      { return WS; }
{NL}                                      { yybegin(YYINITIAL); yypushback(yytext().length()); return SEMICOLON_SYNTHETIC; }
{LINE_COMMENT}                            { return LINE_COMMENT; }
{MULTI_LINE_COMMENT_START}                { yy_push_state(MULTI_LINE_COMMENT_STATE); return MULTI_LINE_COMMENT_START; }
.                                         { yybegin(YYINITIAL); yypushback(yytext().length()); }
}

<ASM_BLOCK> {
{WS}                                      { return WS; }
{NL}+                                     { return NLS; }

"volatile"                                { return VOLATILE; }
{IDENT}                                   { return IDENTIFIER; }

"{"                                       { yybegin(ASM_BLOCK_LINE); return LBRACE; }
}

<ASM_BLOCK_LINE> {
{NL}+                                     { return NLS; }
"}"                                       { yybegin(MAYBE_SEMICOLON); return RBRACE; }
[^}\r\n]+                                 { return ASM_LINE; }
}
