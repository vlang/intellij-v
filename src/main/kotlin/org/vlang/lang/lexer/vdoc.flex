package org.vlang.lang;

import com.intellij.psi.tree.IElementType;
import com.intellij.lexer.FlexLexer;
import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static org.vlang.lang.psi.VlangTokenTypes.*;
import static org.vlang.lang.psi.VlangDocTokenTypes.*;

%%

%{
  public _VlangDocLexer() {
    this((java.io.Reader)null);
 }
%}

%class _VlangDocLexer
%implements FlexLexer, VlangTypes
%unicode
%public

%function advance
%type IElementType

%xstate COMMENT_BODY
%xstate AFTER_CRLF

LETTER = [:letter:] | "_" | "-"
DIGIT =  [:digit:]

WHITE_SPACE_NO_CRLF = [\ \t\f]

IDENT = {LETTER} ({LETTER} | {DIGIT} )*
TAG =   "@"{IDENT}

%%

<YYINITIAL> {
"/**"                                                { yybegin(COMMENT_BODY); return DOC_COMMENT_START;                         }
[^]                                                  { return BAD_CHARACTER;  /* can't happen */                                           }
}

<COMMENT_BODY> {
"*/"                                                 { return zzMarkedPos == zzEndRead ? DOC_COMMENT_END : DOC_COMMENT_BODY; }
{WHITE_SPACE_NO_CRLF}* (\n+ {WHITE_SPACE_NO_CRLF}*)+ { yybegin(AFTER_CRLF); return WS;                                            }
{TAG}                                                { return DOC_COMMENT_TAG;                                                                 }
.                                                    { return DOC_COMMENT_BODY;                                                     }
}

<AFTER_CRLF> {
"*/" | [^"*"]                                        { yypushback(yylength()); yybegin(COMMENT_BODY); break;                               }
"*"                                                  { yybegin(COMMENT_BODY); return DOC_COMMENT_LEADING_ASTERISK;                         }
}
