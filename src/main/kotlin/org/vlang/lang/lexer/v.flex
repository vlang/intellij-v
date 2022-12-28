package org.vlang.lang;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import java.util.Stack;
import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static org.vlang.lang.psi.VlangTokenTypes.*;
import static org.vlang.lang.psi.VlangDocElementTypes.*;

%%

%{
  private static final class State {
    final int lBraceCount;
    final int state;

    public State(int state, int lBraceCount) {
        this.state = state;
        this.lBraceCount = lBraceCount;
    }

    @Override
    public String toString() {
        return "yystate = " + state + (lBraceCount == 0 ? "" : "lBraceCount = " + lBraceCount);
    }
  }

  private final Stack<State> states = new Stack<State>();
  private int lBraceCount;

  private int commentStart;
  private int commentDepth;

  private void pushState(int state) {
    states.push(new State(yystate(), lBraceCount));
    lBraceCount = 0;
    yybegin(state);
  }

  private void popState() {
    State state = states.pop();
    lBraceCount = state.lBraceCount;
    yybegin(state.state);
  }

  private void popMaybeSemicolonState() {
     // if there is nested state with semicolon
     if (states.peek().state == MAYBE_SEMICOLON) {
       states.pop();
     }
     popState();
  }

  public _VlangLexer() {
    this((java.io.Reader)null);
 }
%}

%class _VlangLexer
%implements FlexLexer, VlangTypes
%unicode
%public

%function advance
%type IElementType

%eof{
  return;
%eof}

NL = \n
WS = [ \t\f]

EOL_DOC_COMMENT = ({WS}*"//".*{NL})*({WS}*"//".*)
LINE_COMMENT = "//" [^\r\n]*
HASH_COMMENT = "#" [^\[] [^\r\n]*
SHEBANG = "#!" [^\[] [^\r\n]*
COMPILE_DIRECTIVE = ("#define" | "#flag" | "#include" | "#insert" | "#pkgconfig") [^\r\n]*

MULTI_LINE_DEGENERATE_COMMENT = "/*" "*"+ "/"

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

LONELY_DOLLAR=\$
SHORT_TEMPLATE_ENTRY=\${IDENT}
LONG_TEMPLATE_ENTRY_START=\$\{
REGULAR_STRING_PART=[^\\\"\$]+
REGULAR_SINGLE_STRING_PART=[^\\\'\$]+

%state MAYBE_SEMICOLON

%xstate STRING SINGLE_STRING MULTI_LINE_COMMENT_STATE
%state LONG_TEMPLATE_ENTRY

%state SHORT_TEMPLATE_ENTRY
%state ASM_BLOCK
%state ASM_BLOCK_LINE

%%
// String templates

{C_STR_MODIFIER}? \'                        { pushState(SINGLE_STRING); return OPEN_QUOTE; }
<SINGLE_STRING> \'                          { popState(); pushState(MAYBE_SEMICOLON); return CLOSING_QUOTE; }
<SINGLE_STRING> "\\" (. | "\\")             { return LITERAL_STRING_TEMPLATE_ESCAPE_ENTRY; }
<SINGLE_STRING> {LONG_TEMPLATE_ENTRY_START} { pushState(LONG_TEMPLATE_ENTRY); return LONG_TEMPLATE_ENTRY_START; }

{C_STR_MODIFIER}? \"                        { pushState(STRING); return OPEN_QUOTE; }
<STRING> \"                                 { popState(); pushState(MAYBE_SEMICOLON); return CLOSING_QUOTE; }
<STRING> "\\" (. | "\\")                    { return LITERAL_STRING_TEMPLATE_ESCAPE_ENTRY; }
<STRING> {LONG_TEMPLATE_ENTRY_START}        { pushState(LONG_TEMPLATE_ENTRY); return LONG_TEMPLATE_ENTRY_START; }

<STRING> {REGULAR_STRING_PART}                  { return LITERAL_STRING_TEMPLATE_ENTRY; }
<SINGLE_STRING> {REGULAR_SINGLE_STRING_PART}    { return LITERAL_STRING_TEMPLATE_ENTRY; }

<STRING, SINGLE_STRING> {SHORT_TEMPLATE_ENTRY}  {
                                                   pushState(SHORT_TEMPLATE_ENTRY);
                                                   yypushback(yylength() - 1);
                                                   return SHORT_TEMPLATE_ENTRY_START;
                                                }
<SHORT_TEMPLATE_ENTRY> {IDENT}                  { popState(); return IDENTIFIER; }

<STRING, SINGLE_STRING> {LONELY_DOLLAR}         { return LITERAL_STRING_TEMPLATE_ENTRY; }

<LONG_TEMPLATE_ENTRY> "{"                       { lBraceCount++; return LBRACE; }
<LONG_TEMPLATE_ENTRY> "}"                       {
                                                    if (lBraceCount == 0) {
                                                      popState();
                                                      return TEMPLATE_ENTRY_END;
                                                    }
                                                    lBraceCount--;
                                                    pushState(MAYBE_SEMICOLON);
                                                    return RBRACE;
                                                }

// (Nested) comments

"/**/" {
    return MULTI_LINE_COMMENT;
}

"/*" {
    pushState(MULTI_LINE_COMMENT_STATE);
    commentDepth = 0;
    commentStart = getTokenStart();
}

<MULTI_LINE_COMMENT_STATE> {
    "/*" {
         commentDepth++;
    }

    <<EOF>> {
        int state = yystate();
        popState();
        zzStartRead = commentStart;
        return MULTI_LINE_COMMENT;
    }

    "*/" {
        if (commentDepth > 0) {
            commentDepth--;
        } else {
             int state = yystate();
             popState();
             zzStartRead = commentStart;
             return MULTI_LINE_COMMENT;
        }
    }

    [\s\S] {}
}

// Others

<YYINITIAL> "}" { pushState(MAYBE_SEMICOLON); return RBRACE; }

<YYINITIAL, LONG_TEMPLATE_ENTRY> {
{WS}                                      { return WS; }
{NL}+                                     { return NLS; }

{SHEBANG}                                 { return SHEBANG; }
{EOL_DOC_COMMENT}                         { return DOC_COMMENT; }
{LINE_COMMENT}                            { return LINE_COMMENT; }

{COMPILE_DIRECTIVE}                       { return COMPILE_DIRECTIVE; }
{HASH_COMMENT}                            { return HASH_COMMENT; }

// without this rule /*****/ is parsed as doc comment and /**/ is parsed as not closed doc comment, thanks Dart plugin
{MULTI_LINE_DEGENERATE_COMMENT}           { return MULTI_LINE_COMMENT; }
//{MULTI_LINE_COMMENT_START}                { pushState(MULTI_LINE_COMMENT_STATE); return MULTI_LINE_COMMENT_START; }

"`\\`"                                    { pushState(MAYBE_SEMICOLON); return BAD_CHARACTER; }
"``"                                      { pushState(MAYBE_SEMICOLON); return CHAR; }
"`" [^\\] "`"                             { pushState(MAYBE_SEMICOLON); return CHAR; }
"`" \n "`"?                               { pushState(MAYBE_SEMICOLON); return CHAR; }
"`\\" (. | "\\") "`"                      { pushState(MAYBE_SEMICOLON); return CHAR; }

// \141`, `\342\230\205`
"`" ("\\" {OCT_DIGIT} {3}) {1,3} "`"?     { pushState(MAYBE_SEMICOLON); return CHAR; }
"`" ("\\x" {HEX_DIGIT} {2}) {1,3} "`"?    { pushState(MAYBE_SEMICOLON); return CHAR; }
"`\\u" {HEX_DIGIT} {4} "`"?               { pushState(MAYBE_SEMICOLON); return CHAR; }
"`\\U" {HEX_DIGIT} {8} "`"?               { pushState(MAYBE_SEMICOLON); return CHAR; }

{RAW_DOUBLE_QUOTE_STRING}                 { pushState(MAYBE_SEMICOLON); return RAW_STRING; }
{RAW_SINGLE_QUOTE_STRING}                 { pushState(MAYBE_SEMICOLON); return RAW_STRING; }

"..."                                     { return TRIPLE_DOT; }
".."                                      { return RANGE; }
"."                                       { return DOT; }
"?."                                      { return SAFE_DOT; }
"!."                                      { return SAFE_DOT; }
"~"                                       { return TILDA; }
"|"                                       { return BIT_OR; }
"{"                                       { return LBRACE; }

"#["                                      { return HASH_LBRACK; }

"["                                       { return LBRACK; }
"]"                                       { pushState(MAYBE_SEMICOLON); return RBRACK; }

"("                                       { return LPAREN; }
")"                                       { pushState(MAYBE_SEMICOLON); return RPAREN; }

":"                                       { pushState(MAYBE_SEMICOLON); return COLON; }
";"                                       { return SEMICOLON; }
","                                       { return COMMA; }

"=="                                      { return EQ; }
"="                                       { return ASSIGN; }

"!="                                      { return NOT_EQ; }
"!"                                       { pushState(MAYBE_SEMICOLON); return NOT; }
"?"                                       { pushState(MAYBE_SEMICOLON); return QUESTION; }

"++"                                      { pushState(MAYBE_SEMICOLON); return PLUS_PLUS; }
"+="                                      { return PLUS_ASSIGN; }
"+"                                       { return PLUS; }

"--"                                      { pushState(MAYBE_SEMICOLON); return MINUS_MINUS; }
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
">"                                       { pushState(MAYBE_SEMICOLON); return GREATER; }

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

"return"                                  { pushState(MAYBE_SEMICOLON); return RETURN; }
"select"                                  { return SELECT; }
"match"                                   { return MATCH; }
"or"                                      { return OR; }
"if"                                      { return IF; }
"else"                                    { return ELSE; }
"goto"                                    { return GOTO; }

"assert"                                  { return ASSERT; }

// loop
"for"                                     { return FOR; }
"break"                                   { pushState(MAYBE_SEMICOLON); return BREAK; }
"continue"                                { pushState(MAYBE_SEMICOLON); return CONTINUE; }

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
"nil"                                     { pushState(MAYBE_SEMICOLON); return NIL; }
"true"                                    { pushState(MAYBE_SEMICOLON); return TRUE; }
"false"                                   { pushState(MAYBE_SEMICOLON); return FALSE; }
"none"                                    { pushState(MAYBE_SEMICOLON); return NONE; }

// modifiers
"pub"                                     { return PUB; }
"mut"                                     { return MUT; }
"static"                                  { return STATIC; }
"shared"                                  { return SHARED; }
"volatile"                                { return VOLATILE; }
"atomic"                                  { return ATOMIC; }
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

"asm"                                     { pushState(ASM_BLOCK); return ASM; }

{IDENT}                                   { pushState(MAYBE_SEMICOLON); return IDENTIFIER; }
{SPECIAL_IDENT}                           { pushState(MAYBE_SEMICOLON); return IDENTIFIER; }
"$"{IDENT}                                { pushState(MAYBE_SEMICOLON); return IDENTIFIER; }
"@"{IDENT}                                { pushState(MAYBE_SEMICOLON); return IDENTIFIER; }

\!in{IDENT_PART}                          { yypushback(3); pushState(MAYBE_SEMICOLON); return NOT; }
\!is{IDENT_PART}                          { yypushback(3); pushState(MAYBE_SEMICOLON); return NOT; }

"!in"                                     { return NOT_IN; }
"!is"                                     { return NOT_IS; }

{NUM_FLOAT}                               { pushState(MAYBE_SEMICOLON); return FLOAT; }
{NUM_BIN}                                 { pushState(MAYBE_SEMICOLON); return BIN; }
{NUM_OCT}                                 { pushState(MAYBE_SEMICOLON); return OCT; }
{NUM_HEX}                                 { pushState(MAYBE_SEMICOLON); return HEX; }
{NUM_INT}                                 { pushState(MAYBE_SEMICOLON); return INT; }

.                                         { return BAD_CHARACTER; }
}

<MAYBE_SEMICOLON> {
{WS}                                      { return WS; }
{NL}                                      { popMaybeSemicolonState();
                                            yypushback(yytext().length());
                                            return SEMICOLON_SYNTHETIC; }
{LINE_COMMENT}                            { return LINE_COMMENT; }
//{MULTI_LINE_COMMENT_START}                { pushState(MULTI_LINE_COMMENT_STATE); return MULTI_LINE_COMMENT_START; }
.                                         { popMaybeSemicolonState(); yypushback(yytext().length()); }
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

// error fallback
[\s\S]       { return BAD_CHARACTER; }
// error fallback for exclusive states
<STRING, SINGLE_STRING> .
             { return BAD_CHARACTER; }
