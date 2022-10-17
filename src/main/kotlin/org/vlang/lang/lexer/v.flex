package org.vlang.lang;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import java.util.Stack;
import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static org.vlang.lang.psi.VlangTokenTypes.*;
import static org.vlang.lang.psi.VlangDocTokenTypes.*;

%%

%{
  private Stack<Integer> stack = new Stack<Integer>();
  private Stack<Integer> quotesStack = new Stack<Integer>();
  private Stack<Integer> prevQuotesStack = new Stack<Integer>();
  private int SINGLE_QUOTE = 0;
  private int DOUBLE_QUOTE = 1;

  public void yy_push_state(int state) {
    prevQuotesStack.addAll(quotesStack);
    quotesStack.clear();
    stack.push(state);
    yybegin(state);
  }

  public void yy_pop_state() {
    stack.pop();
    yybegin(stack.lastElement());
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
LANGUAGE_INJECTION_COMMENT = [^\r\n]*
DOC_COMMENT = "/**" ( ([^"*"]|[\r\n])* ("*"+ [^"*""/"] )? )* ("*" | "*"+"/")?
MULTILINE_COMMENT = "/*" ( ([^"*"]|[\r\n])* ("*"+ [^"*""/"] )? )* ("*" | "*"+"/")?

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

IDENT = {LETTER} ({LETTER} | {DIGIT} )*
SPECIAL_IDENT = ("JS." | "C.") {LETTER} ({LETTER} | {DIGIT} | "." )*

STR_DOUBLE = "\""
STR_SINGLE = "'"
STR_MODIFIER = "c"
RAW_STR_MODIFIER = "r"

RAW_DOUBLE_QUOTE_STRING = {RAW_STR_MODIFIER} {STR_DOUBLE} [^\"]* {STR_DOUBLE}
RAW_SINGLE_QUOTE_STRING = {RAW_STR_MODIFIER} {STR_SINGLE} [^\']* {STR_SINGLE}

%state MAYBE_SEMICOLON
%state TEMPLATE_STRING
%state SHORT_TEMPLATE_ENTRY
%state SHORT_TEMPLATE_ENTRY_FIELD_NAME
%state ASM_BLOCK
%state ASM_BLOCK_LINE
%state SQL_BLOCK
%state SQL_BLOCK_LINE

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
"${"         { yy_push_state(YYINITIAL);      return LONG_TEMPLATE_ENTRY_START; }
"$"          { yybegin(SHORT_TEMPLATE_ENTRY); return SHORT_TEMPLATE_ENTRY_START; }

{STR_DOUBLE} { return handle_string_end(true); }
{STR_SINGLE} { return handle_string_end(false); }

"\\" (. | "\\") { return LITERAL_STRING_TEMPLATE_ESCAPE_ENTRY; }
[^\"\'\\$]+     { return LITERAL_STRING_TEMPLATE_ENTRY; }
}

<YYINITIAL> {
{WS}                                      { return WS; }
{NL}+                                     { return NLS; }

{LINE_COMMENT}                            { return LINE_COMMENT; }
{DOC_COMMENT}                             { return DOC_COMMENT; }
{MULTILINE_COMMENT}                       { return MULTILINE_COMMENT; }

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

{STR_MODIFIER}? {STR_DOUBLE} {
    yy_push_state(TEMPLATE_STRING);
    yy_start_string(DOUBLE_QUOTE);
    return OPEN_QUOTE;
}

{STR_MODIFIER}? {STR_SINGLE} {
    yy_push_state(TEMPLATE_STRING);
    yy_start_string(SINGLE_QUOTE);
    return OPEN_QUOTE;
}

"..."                                     { return TRIPLE_DOT; }
".."                                      { return RANGE; }
"."                                       { return DOT; }
"~"                                       { return TILDA; }
"|"                                       { return BIT_OR; }
"{"                                       { return LBRACE; }
"}" {
    if (inside_interpolation()) {
        yy_pop_state();
        return LONG_TEMPLATE_ENTRY_END;
    }

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
// ">>"                                      { return SHIFT_RIGHT; } // done in parser <<gtGt>>
">="                                      { return GREATER_OR_EQUAL; }
">"                                       { yybegin(MAYBE_SEMICOLON); return GREATER; }

"`"                                       { return BACKTICK; }

":="                                      { return VAR_ASSIGN; }

"$for"                                    { return FOR_COMPILE_TIME ; }
"$if"                                     { return IF_COMPILE_TIME ; }
"$else"                                   { return ELSE_COMPILE_TIME ; }

"nil"                                     { yybegin(MAYBE_SEMICOLON); return NIL; }
"true"                                    { yybegin(MAYBE_SEMICOLON); return TRUE; }
"false"                                   { yybegin(MAYBE_SEMICOLON); return FALSE; }

"assert"                                  { return ASSERT; }
"break"                                   { yybegin(MAYBE_SEMICOLON); return BREAK; }
"fallthrough"                             { yybegin(MAYBE_SEMICOLON); return FALLTHROUGH; }
"return"                                  { yybegin(MAYBE_SEMICOLON); return RETURN ; }
"continue"                                { yybegin(MAYBE_SEMICOLON); return CONTINUE ; }

"unsafe"                                  { return UNSAFE; }
"default"                                 { return DEFAULT; }
"module"                                  { return MODULE; }
"pub"                                     { return PUB; }
"fn"                                      { return FN; }
"interface"                               { return INTERFACE; }
"select"                                  { return SELECT; }

"defer"                                   { return DEFER; }
"go"                                      { return GO; }

"shared"                                  { return SHARED; }
"rlock"                                   { return RLOCK; }
"lock"                                    { return LOCK; }

"chan"                                    { return CHAN; }

"union"                                   { return UNION; }
"struct"                                  { return STRUCT; }
"enum"                                    { return ENUM; }
"else"                                    { return ELSE; }
"goto"                                    { return GOTO; }
"switch"                                  { return SWITCH; }
"const"                                   { return CONST; }

"or"                                      { return OR ; }
"match"                                   { return MATCH ; }
"if"                                      { return IF ; }
"for"                                     { return FOR ; }
"import"                                  { return IMPORT ; }
"as"                                      { return AS ; }

"in"                                      { return IN; }
"is"                                      { return IS; }
"type"                                    { return TYPE_; }
"mut"                                     { return MUT; }

"volatile"                                { return VOLATILE; }
"asm"                                     { yybegin(ASM_BLOCK); return ASM; }
"sql"                                     { yybegin(SQL_BLOCK); return SQL; }

"__global"                                { return BUILTIN_GLOBAL; }

^"#" {LANGUAGE_INJECTION_COMMENT}         { yybegin(MAYBE_SEMICOLON); return LANGUAGE_INJECTION; }
{WS}+ "#" {LANGUAGE_INJECTION_COMMENT}    { yybegin(MAYBE_SEMICOLON); return LANGUAGE_INJECTION; }

{SPECIAL_IDENT}                           { yybegin(MAYBE_SEMICOLON); return IDENTIFIER; }
{IDENT}                                   { yybegin(MAYBE_SEMICOLON); return IDENTIFIER; }
"$"{IDENT}                                { yybegin(MAYBE_SEMICOLON); return IDENTIFIER; }
"@"{IDENT}                                { yybegin(MAYBE_SEMICOLON); return IDENTIFIER; }

"!" "in" {WS}+                            { return NOT_IN; }
"!" "is" {WS}+                            { return NOT_IS; }

{NUM_FLOAT}"i"                            { yybegin(MAYBE_SEMICOLON); return FLOATI; }
{NUM_FLOAT}                               { yybegin(MAYBE_SEMICOLON); return FLOAT; }
{DIGIT}+"i"                               { yybegin(MAYBE_SEMICOLON); return DECIMALI; }
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
{MULTILINE_COMMENT}                       { return MULTILINE_COMMENT; }
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

<SQL_BLOCK> {
{WS}                                      { return WS; }
{NL}+                                     { return NLS; }

// hack
":"                                       { yybegin(YYINITIAL); return COLON; }

{IDENT}                                   { return IDENTIFIER; }

"{"                                       { yybegin(SQL_BLOCK_LINE); return LBRACE; }
}

<SQL_BLOCK_LINE> {
{NL}+                                     { return NLS; }
"}"                                       { yybegin(MAYBE_SEMICOLON); return RBRACE; }
[^}\r\n]+                                 { return SQL_LINE; }
}
