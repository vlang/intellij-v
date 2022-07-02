// This is a generated file. Not intended for manual editing.
package org.vlang.lang;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LightPsiParser;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

import static org.vlang.lang.VlangParserUtil.*;
import static org.vlang.lang.VlangTypes.*;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class VlangParser implements PsiParser, LightPsiParser {

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return File(b, l + 1);
  }

  /* ********************************************************** */
  // '[' AttributeExpression ']'
  public static boolean Attribute(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Attribute")) return false;
    if (!nextTokenIs(b, LBRACK)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ATTRIBUTE, null);
    r = consumeToken(b, LBRACK);
    p = r; // pin = 1
    r = r && report_error_(b, AttributeExpression(b, l + 1));
    r = p && consumeToken(b, RBRACK) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // IfAttribute | PlainAttribute
  public static boolean AttributeExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AttributeExpression")) return false;
    if (!nextTokenIs(b, "<attribute expression>", IDENTIFIER, IF)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ATTRIBUTE_EXPRESSION, "<attribute expression>");
    r = IfAttribute(b, l + 1);
    if (!r) r = PlainAttribute(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // Attribute (semi Attribute)* '<NL>'?
  public static boolean Attributes(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Attributes")) return false;
    if (!nextTokenIs(b, LBRACK)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ATTRIBUTES, null);
    r = Attribute(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, Attributes_1(b, l + 1));
    r = p && Attributes_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[] {
    create_token_set_(RANGE_CLAUSE, SHORT_VAR_DECLARATION),
    create_token_set_(PAR_TYPE, TYPE, TYPE_LIST),
    create_token_set_(ASSIGNMENT_STATEMENT, ELSE_STATEMENT, FOR_STATEMENT, IF_STATEMENT,
      INC_DEC_STATEMENT, RETURN_STATEMENT, SIMPLE_STATEMENT, STATEMENT),
    create_token_set_(ADD_EXPR, AND_EXPR, CALL_EXPR, CONDITIONAL_EXPR,
      EXPRESSION, LITERAL, MUL_EXPR, OR_EXPR,
      PARENTHESES_EXPR, REFERENCE_EXPRESSION, STRING_LITERAL, UNARY_EXPR),
  };

  /* ********************************************************** */
  // '+' | '-' | '|' | '^'
  static boolean AddOp(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AddOp")) return false;
    boolean r;
    r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, MINUS);
    if (!r) r = consumeToken(b, BIT_OR);
    if (!r) r = consumeToken(b, BIT_XOR);
    return r;
  }

  /* ********************************************************** */
  // '(' [ ExpressionArgList '...'? ','? ] ')'
  public static boolean ArgumentList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArgumentList")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ARGUMENT_LIST, null);
    r = consumeToken(b, LPAREN);
    p = r; // pin = 1
    r = r && report_error_(b, ArgumentList_1(b, l + 1));
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ ExpressionArgList '...'? ','? ]
  private static boolean ArgumentList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArgumentList_1")) return false;
    ArgumentList_1_0(b, l + 1);
    return true;
  }

  // ExpressionArgList '...'? ','?
  private static boolean ArgumentList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArgumentList_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ExpressionArgList(b, l + 1);
    r = r && ArgumentList_1_0_1(b, l + 1);
    r = r && ArgumentList_1_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '...'?
  private static boolean ArgumentList_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArgumentList_1_0_1")) return false;
    consumeToken(b, TRIPLE_DOT);
    return true;
  }

  // ','?
  private static boolean ArgumentList_1_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArgumentList_1_0_2")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // assign_op ExpressionList
  public static boolean AssignmentStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AssignmentStatement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _LEFT_, ASSIGNMENT_STATEMENT, "<assignment statement>");
    r = assign_op(b, l + 1);
    p = r; // pin = 1
    r = r && ExpressionList(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (semi Attribute)*
  private static boolean Attributes_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Attributes_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Attributes_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "Attributes_1", c)) break;
    }
    return true;
  }

  // semi Attribute
  private static boolean Attributes_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Attributes_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = semi(b, l + 1);
    r = r && Attribute(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '<NL>'?
  private static boolean Attributes_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Attributes_2")) return false;
    consumeToken(b, SEMICOLON_SYNTHETIC);
    return true;
  }

  /* ********************************************************** */
  // ImportList TopLevelDeclaration*
  static boolean File(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "File")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = ImportList(b, l + 1);
    p = r; // pin = 1
    r = r && File_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // TopLevelDeclaration*
  private static boolean File_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "File_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!TopLevelDeclaration(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "File_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // SimpleStatement? ';' Expression? ';' SimpleStatement?
  public static boolean ForClause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ForClause")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FOR_CLAUSE, "<for clause>");
    r = ForClause_0(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    r = r && ForClause_2(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    r = r && ForClause_4(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // BlockInner
  public static boolean Block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Block")) return false;
    if (!nextTokenIs(b, LBRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = BlockInner(b, l + 1);
    exit_section_(b, m, BLOCK, r);
    return r;
  }

  /* ********************************************************** */
  // '{' ('}' | Statements /*| (<<withOff Statements "BLOCK?" "PAR">> | (!() Statements)) */'}')
  static boolean BlockInner(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BlockInner")) return false;
    if (!nextTokenIs(b, LBRACE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, LBRACE);
    p = r; // pin = 1
    r = r && BlockInner_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // '}' | Statements /*| (<<withOff Statements "BLOCK?" "PAR">> | (!() Statements)) */'}'
  private static boolean BlockInner_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BlockInner_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, RBRACE);
    if (!r) r = BlockInner_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // Statements /*| (<<withOff Statements "BLOCK?" "PAR">> | (!() Statements)) */'}'
  private static boolean BlockInner_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BlockInner_1_1")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = Statements(b, l + 1);
    p = r; // pin = 1
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // BlockInner
  public static boolean BlockWithConsume(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BlockWithConsume")) return false;
    if (!nextTokenIs(b, LBRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = BlockInner(b, l + 1);
    exit_section_(b, m, BLOCK, r);
    return r;
  }

  /* ********************************************************** */
  // SimpleStatementOpt Expression?
  static boolean Condition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Condition")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = SimpleStatementOpt(b, l + 1);
    r = r && Condition_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // Expression?
  private static boolean Condition_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Condition_1")) return false;
    Expression(b, l + 1, -1);
    return true;
  }

  /* ********************************************************** */
  // else (IfStatement | Block)
  public static boolean ElseStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ElseStatement")) return false;
    if (!nextTokenIs(b, ELSE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ELSE_STATEMENT, null);
    r = consumeToken(b, ELSE);
    p = r; // pin = 1
    r = r && ElseStatement_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // IfStatement | Block
  private static boolean ElseStatement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ElseStatement_1")) return false;
    boolean r;
    r = IfStatement(b, l + 1);
    if (!r) r = Block(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // ExpressionOrTypeWithRecover2 (',' (ExpressionOrTypeWithRecover2 | &')'))*
  static boolean ExpressionArgList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ExpressionArgList")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = ExpressionOrTypeWithRecover2(b, l + 1);
    p = r; // pin = 1
    r = r && ExpressionArgList_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (',' (ExpressionOrTypeWithRecover2 | &')'))*
  private static boolean ExpressionArgList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ExpressionArgList_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ExpressionArgList_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ExpressionArgList_1", c)) break;
    }
    return true;
  }

  // ',' (ExpressionOrTypeWithRecover2 | &')')
  private static boolean ExpressionArgList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ExpressionArgList_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && ExpressionArgList_1_0_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ExpressionOrTypeWithRecover2 | &')'
  private static boolean ExpressionArgList_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ExpressionArgList_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ExpressionOrTypeWithRecover2(b, l + 1);
    if (!r) r = ExpressionArgList_1_0_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // &')'
  private static boolean ExpressionArgList_1_0_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ExpressionArgList_1_0_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = consumeToken(b, RPAREN);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ExpressionWithRecover (',' (ExpressionWithRecover | &')'))*
  static boolean ExpressionList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ExpressionList")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = ExpressionWithRecover(b, l + 1);
    p = r; // pin = 1
    r = r && ExpressionList_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (',' (ExpressionWithRecover | &')'))*
  private static boolean ExpressionList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ExpressionList_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ExpressionList_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ExpressionList_1", c)) break;
    }
    return true;
  }

  // ',' (ExpressionWithRecover | &')')
  private static boolean ExpressionList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ExpressionList_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && ExpressionList_1_0_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ExpressionWithRecover | &')'
  private static boolean ExpressionList_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ExpressionList_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ExpressionWithRecover(b, l + 1);
    if (!r) r = ExpressionList_1_0_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // &')'
  private static boolean ExpressionList_1_0_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ExpressionList_1_0_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = consumeToken(b, RPAREN);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // !('!' | '!=' | '%' | '%=' | '&&' | '&' | '&=' | '&^' | '&^=' | '(' | ')' | '*' | '*=' | '+' | '++' | '+=' | ',' | '-' | '--' | '-=' | '...' | '/' | '/=' | ':' | ';' | '<' | '<-' | '<<' | '<<=' | '<=' | '<NL>' | '=' | '==' | '>' | '>=' | '>>' | '>>=' | '[' | ']' | '^' | '^=' | 'type' | '{' | '|' | '|=' | '||' | '}' | break | case | chan | char | const | continue | decimali | default | defer | else | fallthrough | float | floati | for | func | go | goto | hex | identifier | if | int | interface | map | oct | return | select | string | raw_string | struct | switch | var)
  static boolean ExpressionListRecover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ExpressionListRecover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !ExpressionListRecover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '!' | '!=' | '%' | '%=' | '&&' | '&' | '&=' | '&^' | '&^=' | '(' | ')' | '*' | '*=' | '+' | '++' | '+=' | ',' | '-' | '--' | '-=' | '...' | '/' | '/=' | ':' | ';' | '<' | '<-' | '<<' | '<<=' | '<=' | '<NL>' | '=' | '==' | '>' | '>=' | '>>' | '>>=' | '[' | ']' | '^' | '^=' | 'type' | '{' | '|' | '|=' | '||' | '}' | break | case | chan | char | const | continue | decimali | default | defer | else | fallthrough | float | floati | for | func | go | goto | hex | identifier | if | int | interface | map | oct | return | select | string | raw_string | struct | switch | var
  private static boolean ExpressionListRecover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ExpressionListRecover_0")) return false;
    boolean r;
    r = consumeToken(b, NOT);
    if (!r) r = consumeToken(b, NOT_EQ);
    if (!r) r = consumeToken(b, REMAINDER);
    if (!r) r = consumeToken(b, REMAINDER_ASSIGN);
    if (!r) r = consumeToken(b, COND_AND);
    if (!r) r = consumeToken(b, BIT_AND);
    if (!r) r = consumeToken(b, BIT_AND_ASSIGN);
    if (!r) r = consumeToken(b, BIT_CLEAR);
    if (!r) r = consumeToken(b, BIT_CLEAR_ASSIGN);
    if (!r) r = consumeToken(b, LPAREN);
    if (!r) r = consumeToken(b, RPAREN);
    if (!r) r = consumeToken(b, MUL);
    if (!r) r = consumeToken(b, MUL_ASSIGN);
    if (!r) r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, PLUS_PLUS);
    if (!r) r = consumeToken(b, PLUS_ASSIGN);
    if (!r) r = consumeToken(b, COMMA);
    if (!r) r = consumeToken(b, MINUS);
    if (!r) r = consumeToken(b, MINUS_MINUS);
    if (!r) r = consumeToken(b, MINUS_ASSIGN);
    if (!r) r = consumeToken(b, TRIPLE_DOT);
    if (!r) r = consumeToken(b, QUOTIENT);
    if (!r) r = consumeToken(b, QUOTIENT_ASSIGN);
    if (!r) r = consumeToken(b, COLON);
    if (!r) r = consumeToken(b, SEMICOLON);
    if (!r) r = consumeToken(b, LESS);
    if (!r) r = consumeToken(b, SEND_CHANNEL);
    if (!r) r = consumeToken(b, SHIFT_LEFT);
    if (!r) r = consumeToken(b, SHIFT_LEFT_ASSIGN);
    if (!r) r = consumeToken(b, LESS_OR_EQUAL);
    if (!r) r = consumeToken(b, SEMICOLON_SYNTHETIC);
    if (!r) r = consumeToken(b, ASSIGN);
    if (!r) r = consumeToken(b, EQ);
    if (!r) r = consumeToken(b, GREATER);
    if (!r) r = consumeToken(b, GREATER_OR_EQUAL);
    if (!r) r = consumeToken(b, SHIFT_RIGHT);
    if (!r) r = consumeToken(b, SHIFT_RIGHT_ASSIGN);
    if (!r) r = consumeToken(b, LBRACK);
    if (!r) r = consumeToken(b, RBRACK);
    if (!r) r = consumeToken(b, BIT_XOR);
    if (!r) r = consumeToken(b, BIT_XOR_ASSIGN);
    if (!r) r = consumeToken(b, TYPE_);
    if (!r) r = consumeToken(b, LBRACE);
    if (!r) r = consumeToken(b, BIT_OR);
    if (!r) r = consumeToken(b, BIT_OR_ASSIGN);
    if (!r) r = consumeToken(b, COND_OR);
    if (!r) r = consumeToken(b, RBRACE);
    if (!r) r = consumeToken(b, BREAK);
    if (!r) r = consumeToken(b, CASE);
    if (!r) r = consumeToken(b, CHAN);
    if (!r) r = consumeToken(b, CHAR);
    if (!r) r = consumeToken(b, CONST);
    if (!r) r = consumeToken(b, CONTINUE);
    if (!r) r = consumeToken(b, DECIMALI);
    if (!r) r = consumeToken(b, DEFAULT);
    if (!r) r = consumeToken(b, DEFER);
    if (!r) r = consumeToken(b, ELSE);
    if (!r) r = consumeToken(b, FALLTHROUGH);
    if (!r) r = consumeToken(b, FLOAT);
    if (!r) r = consumeToken(b, FLOATI);
    if (!r) r = consumeToken(b, FOR);
    if (!r) r = consumeToken(b, FUNC);
    if (!r) r = consumeToken(b, GO);
    if (!r) r = consumeToken(b, GOTO);
    if (!r) r = consumeToken(b, HEX);
    if (!r) r = consumeToken(b, IDENTIFIER);
    if (!r) r = consumeToken(b, IF);
    if (!r) r = consumeToken(b, INT);
    if (!r) r = consumeToken(b, INTERFACE);
    if (!r) r = consumeToken(b, MAP);
    if (!r) r = consumeToken(b, OCT);
    if (!r) r = consumeToken(b, RETURN);
    if (!r) r = consumeToken(b, SELECT);
    if (!r) r = consumeToken(b, STRING);
    if (!r) r = consumeToken(b, RAW_STRING);
    if (!r) r = consumeToken(b, STRUCT);
    if (!r) r = consumeToken(b, SWITCH);
    if (!r) r = consumeToken(b, VAR);
    return r;
  }

  /* ********************************************************** */
  // Expression
  static boolean ExpressionOrLiteralTypeExpr(PsiBuilder b, int l) {
    return Expression(b, l + 1, -1);
  }

  /* ********************************************************** */
  // ExpressionOrLiteralTypeExpr
  static boolean ExpressionOrTypeWithRecover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ExpressionOrTypeWithRecover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = ExpressionOrLiteralTypeExpr(b, l + 1);
    exit_section_(b, l, m, r, false, VlangParser::ExpressionListRecover);
    return r;
  }

  /* ********************************************************** */
  // ExpressionOrTypeWithRecover/*>>*/ | (!() ExpressionOrLiteralTypeExpr)
  static boolean ExpressionOrTypeWithRecover2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ExpressionOrTypeWithRecover2")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = ExpressionOrTypeWithRecover(b, l + 1);
    if (!r) r = ExpressionOrTypeWithRecover2_1(b, l + 1);
    exit_section_(b, l, m, r, false, VlangParser::ExpressionListRecover);
    return r;
  }

  // !() ExpressionOrLiteralTypeExpr
  private static boolean ExpressionOrTypeWithRecover2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ExpressionOrTypeWithRecover2_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ExpressionOrTypeWithRecover2_1_0(b, l + 1);
    r = r && ExpressionOrLiteralTypeExpr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !()
  private static boolean ExpressionOrTypeWithRecover2_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ExpressionOrTypeWithRecover2_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !ExpressionOrTypeWithRecover2_1_0_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ()
  private static boolean ExpressionOrTypeWithRecover2_1_0_0(PsiBuilder b, int l) {
    return true;
  }

  /* ********************************************************** */
  // Expression
  static boolean ExpressionWithRecover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ExpressionWithRecover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = Expression(b, l + 1, -1);
    exit_section_(b, l, m, r, false, VlangParser::ExpressionListRecover);
    return r;
  }

  // SimpleStatement?
  private static boolean ForClause_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ForClause_0")) return false;
    SimpleStatement(b, l + 1);
    return true;
  }

  // Expression?
  private static boolean ForClause_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ForClause_2")) return false;
    Expression(b, l + 1, -1);
    return true;
  }

  /* ********************************************************** */
  // Attributes? SymbolVisibility func identifier Signature BlockWithConsume?
  public static boolean FunctionDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FunctionDeclaration")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_DECLARATION, "<function declaration>");
    r = FunctionDeclaration_0(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, SymbolVisibility(b, l + 1));
    r = p && report_error_(b, consumeTokens(b, -1, FUNC, IDENTIFIER)) && r;
    r = p && report_error_(b, Signature(b, l + 1)) && r;
    r = p && FunctionDeclaration_5(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Attributes?
  private static boolean FunctionDeclaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FunctionDeclaration_0")) return false;
    Attributes(b, l + 1);
    return true;
  }

  // BlockWithConsume?
  private static boolean FunctionDeclaration_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FunctionDeclaration_5")) return false;
    BlockWithConsume(b, l + 1);
    return true;
  }

  // SimpleStatement?
  private static boolean ForClause_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ForClause_4")) return false;
    SimpleStatement(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // ForClause | RangeClause
  static boolean ForOrRangeClause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ForOrRangeClause")) return false;
    boolean r;
    r = ForClause(b, l + 1);
    if (!r) r = RangeClause(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // for (ForOrRangeClause Block | Block | Expression Block)
  public static boolean ForStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ForStatement")) return false;
    if (!nextTokenIs(b, FOR)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FOR_STATEMENT, null);
    r = consumeToken(b, FOR);
    p = r; // pin = for|ForOrRangeClause
    r = r && ForStatement_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ForOrRangeClause Block | Block | Expression Block
  private static boolean ForStatement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ForStatement_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ForStatement_1_0(b, l + 1);
    if (!r) r = Block(b, l + 1);
    if (!r) r = ForStatement_1_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ForOrRangeClause Block
  private static boolean ForStatement_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ForStatement_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = ForOrRangeClause(b, l + 1);
    p = r; // pin = for|ForOrRangeClause
    r = r && Block(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Expression Block
  private static boolean ForStatement_1_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ForStatement_1_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = Expression(b, l + 1, -1);
    r = r && Block(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // if identifier
  public static boolean IfAttribute(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IfAttribute")) return false;
    if (!nextTokenIs(b, IF)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, IF_ATTRIBUTE, null);
    r = consumeTokens(b, 1, IF, IDENTIFIER);
    p = r; // pin = 1
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // func Receiver identifier Signature BlockWithConsume?
  public static boolean MethodDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MethodDeclaration")) return false;
    if (!nextTokenIs(b, FUNC)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, METHOD_DECLARATION, null);
    r = consumeToken(b, FUNC);
    p = r; // pin = 1
    r = r && report_error_(b, Receiver(b, l + 1));
    r = p && report_error_(b, consumeToken(b, IDENTIFIER)) && r;
    r = p && report_error_(b, Signature(b, l + 1)) && r;
    r = p && MethodDeclaration_4(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // BlockWithConsume?
  private static boolean MethodDeclaration_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MethodDeclaration_4")) return false;
    BlockWithConsume(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // identifier [':' StringLiteral]
  public static boolean PlainAttribute(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PlainAttribute")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    r = r && PlainAttribute_1(b, l + 1);
    exit_section_(b, m, PLAIN_ATTRIBUTE, r);
    return r;
  }

  /* ********************************************************** */
  // if Condition Block ElseStatement?
  public static boolean IfStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IfStatement")) return false;
    if (!nextTokenIs(b, IF)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, IF_STATEMENT, null);
    r = consumeToken(b, IF);
    p = r; // pin = 1
    r = r && report_error_(b, Condition(b, l + 1));
    r = p && report_error_(b, Block(b, l + 1)) && r;
    r = p && IfStatement_3(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ElseStatement?
  private static boolean IfStatement_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IfStatement_3")) return false;
    ElseStatement(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // identifier
  public static boolean ImportAlias(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ImportAlias")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, IMPORT_ALIAS, r);
    return r;
  }

  /* ********************************************************** */
  // import ImportSpec
  public static boolean ImportDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ImportDeclaration")) return false;
    if (!nextTokenIs(b, IMPORT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, IMPORT_DECLARATION, null);
    r = consumeToken(b, IMPORT);
    p = r; // pin = 1
    r = r && ImportSpec(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // (ImportDeclaration semi)*
  public static boolean ImportList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ImportList")) return false;
    Marker m = enter_section_(b, l, _NONE_, IMPORT_LIST, "<import list>");
    while (true) {
      int c = current_position_(b);
      if (!ImportList_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ImportList", c)) break;
    }
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  // ImportDeclaration semi
  private static boolean ImportList_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ImportList_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = ImportDeclaration(b, l + 1);
    p = r; // pin = 1
    r = r && semi(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // ImportString ('.' ImportString)* (SelectiveImportList | as ImportAlias)?
  public static boolean ImportSpec(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ImportSpec")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ImportString(b, l + 1);
    r = r && ImportSpec_1(b, l + 1);
    r = r && ImportSpec_2(b, l + 1);
    exit_section_(b, m, IMPORT_SPEC, r);
    return r;
  }

  // ('.' ImportString)*
  private static boolean ImportSpec_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ImportSpec_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ImportSpec_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ImportSpec_1", c)) break;
    }
    return true;
  }

  // '.' ImportString
  private static boolean ImportSpec_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ImportSpec_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DOT);
    r = r && ImportString(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (SelectiveImportList | as ImportAlias)?
  private static boolean ImportSpec_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ImportSpec_2")) return false;
    ImportSpec_2_0(b, l + 1);
    return true;
  }

  // SelectiveImportList | as ImportAlias
  private static boolean ImportSpec_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ImportSpec_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = SelectiveImportList(b, l + 1);
    if (!r) r = ImportSpec_2_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // as ImportAlias
  private static boolean ImportSpec_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ImportSpec_2_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, AS);
    r = r && ImportAlias(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // identifier
  static boolean ImportString(PsiBuilder b, int l) {
    return consumeToken(b, IDENTIFIER);
  }

  /* ********************************************************** */
  // Expression ('++' | '--')
  public static boolean IncDecStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IncDecStatement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, INC_DEC_STATEMENT, "<inc dec statement>");
    r = Expression(b, l + 1, -1);
    r = r && IncDecStatement_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '++' | '--'
  private static boolean IncDecStatement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IncDecStatement_1")) return false;
    boolean r;
    r = consumeToken(b, PLUS_PLUS);
    if (!r) r = consumeToken(b, MINUS_MINUS);
    return r;
  }

  /* ********************************************************** */
  // ExpressionList
  public static boolean LeftHandExprList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "LeftHandExprList")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LEFT_HAND_EXPR_LIST, "<left hand expr list>");
    r = ExpressionList(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [':' StringLiteral]
  private static boolean PlainAttribute_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PlainAttribute_1")) return false;
    PlainAttribute_1_0(b, l + 1);
    return true;
  }

  // ':' StringLiteral
  private static boolean PlainAttribute_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PlainAttribute_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && StringLiteral(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '*' | '/' | '%' | '<<' | '>>' | '&' | '&^'
  static boolean MulOp(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MulOp")) return false;
    boolean r;
    r = consumeToken(b, MUL);
    if (!r) r = consumeToken(b, QUOTIENT);
    if (!r) r = consumeToken(b, REMAINDER);
    if (!r) r = consumeToken(b, SHIFT_LEFT);
    if (!r) r = consumeToken(b, SHIFT_RIGHT);
    if (!r) r = consumeToken(b, BIT_AND);
    if (!r) r = consumeToken(b, BIT_CLEAR);
    return r;
  }

  /* ********************************************************** */
  // package identifier
  public static boolean PackageClause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PackageClause")) return false;
    if (!nextTokenIs(b, PACKAGE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PACKAGE_CLAUSE, null);
    r = consumeTokens(b, 1, PACKAGE, IDENTIFIER);
    p = r; // pin = 1
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // '(' Type ')'
  public static boolean ParType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParType")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && Type(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, PAR_TYPE, r);
    return r;
  }

  /* ********************************************************** */
  // identifier
  public static boolean ParamDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParamDefinition")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, PARAM_DEFINITION, r);
    return r;
  }

  /* ********************************************************** */
  // ParamDefinition &(!('.' | ')')) (',' ParamDefinition)*
  static boolean ParamDefinitionListNoPin(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParamDefinitionListNoPin")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ParamDefinition(b, l + 1);
    r = r && ParamDefinitionListNoPin_1(b, l + 1);
    r = r && ParamDefinitionListNoPin_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // &(!('.' | ')'))
  private static boolean ParamDefinitionListNoPin_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParamDefinitionListNoPin_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = ParamDefinitionListNoPin_1_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // !('.' | ')')
  private static boolean ParamDefinitionListNoPin_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParamDefinitionListNoPin_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !ParamDefinitionListNoPin_1_0_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '.' | ')'
  private static boolean ParamDefinitionListNoPin_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParamDefinitionListNoPin_1_0_0")) return false;
    boolean r;
    r = consumeToken(b, DOT);
    if (!r) r = consumeToken(b, RPAREN);
    return r;
  }

  // (',' ParamDefinition)*
  private static boolean ParamDefinitionListNoPin_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParamDefinitionListNoPin_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ParamDefinitionListNoPin_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ParamDefinitionListNoPin_2", c)) break;
    }
    return true;
  }

  // ',' ParamDefinition
  private static boolean ParamDefinitionListNoPin_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParamDefinitionListNoPin_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && ParamDefinition(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ParamDefinitionListNoPin? '...'? Type | Type
  public static boolean ParameterDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParameterDeclaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PARAMETER_DECLARATION, "<parameter declaration>");
    r = ParameterDeclaration_0(b, l + 1);
    if (!r) r = Type(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ParamDefinitionListNoPin? '...'? Type
  private static boolean ParameterDeclaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParameterDeclaration_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ParameterDeclaration_0_0(b, l + 1);
    r = r && ParameterDeclaration_0_1(b, l + 1);
    r = r && Type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ParamDefinitionListNoPin?
  private static boolean ParameterDeclaration_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParameterDeclaration_0_0")) return false;
    ParamDefinitionListNoPin(b, l + 1);
    return true;
  }

  // '...'?
  private static boolean ParameterDeclaration_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParameterDeclaration_0_1")) return false;
    consumeToken(b, TRIPLE_DOT);
    return true;
  }

  /* ********************************************************** */
  // ParameterDeclaration (',' (ParameterDeclaration | &')'))*
  static boolean ParameterList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParameterList")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = ParameterDeclaration(b, l + 1);
    p = r; // pin = 1
    r = r && ParameterList_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (',' (ParameterDeclaration | &')'))*
  private static boolean ParameterList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParameterList_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ParameterList_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ParameterList_1", c)) break;
    }
    return true;
  }

  // ',' (ParameterDeclaration | &')')
  private static boolean ParameterList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParameterList_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && ParameterList_1_0_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ParameterDeclaration | &')'
  private static boolean ParameterList_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParameterList_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ParameterDeclaration(b, l + 1);
    if (!r) r = ParameterList_1_0_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // &')'
  private static boolean ParameterList_1_0_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParameterList_1_0_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = consumeToken(b, RPAREN);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '(' [ (ParameterList ','?| TypeListNoPin) ] ')'
  public static boolean Parameters(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Parameters")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PARAMETERS, null);
    r = consumeToken(b, LPAREN);
    p = r; // pin = 1
    r = r && report_error_(b, Parameters_1(b, l + 1));
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ (ParameterList ','?| TypeListNoPin) ]
  private static boolean Parameters_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Parameters_1")) return false;
    Parameters_1_0(b, l + 1);
    return true;
  }

  // ParameterList ','?| TypeListNoPin
  private static boolean Parameters_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Parameters_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = Parameters_1_0_0(b, l + 1);
    if (!r) r = TypeListNoPin(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ParameterList ','?
  private static boolean Parameters_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Parameters_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ParameterList(b, l + 1);
    r = r && Parameters_1_0_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ','?
  private static boolean Parameters_1_0_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Parameters_1_0_0_1")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // in Expression | VarDefinitionList in Expression
  public static boolean RangeClause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RangeClause")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, RANGE_CLAUSE, "<range clause>");
    r = RangeClause_0(b, l + 1);
    if (!r) r = RangeClause_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // in Expression
  private static boolean RangeClause_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RangeClause_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IN);
    r = r && Expression(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // VarDefinitionList in Expression
  private static boolean RangeClause_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RangeClause_1")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = VarDefinitionList(b, l + 1);
    r = r && consumeToken(b, IN);
    p = r; // pin = 2
    r = r && Expression(b, l + 1, -1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // '.' identifier
  public static boolean QualifiedReferenceExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "QualifiedReferenceExpression")) return false;
    if (!nextTokenIs(b, DOT)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_, REFERENCE_EXPRESSION, null);
    r = consumeTokens(b, 0, DOT, IDENTIFIER);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '.' identifier
  public static boolean QualifiedTypeReferenceExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "QualifiedTypeReferenceExpression")) return false;
    if (!nextTokenIs(b, DOT)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_, TYPE_REFERENCE_EXPRESSION, null);
    r = consumeTokens(b, 0, DOT, IDENTIFIER);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // VarDefinitionList ':=' ExpressionList
  public static boolean ShortVarDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShortVarDeclaration")) return false;
    if (!nextTokenIs(b, "<short var declaration>", IDENTIFIER, MUT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SHORT_VAR_DECLARATION, "<short var declaration>");
    r = VarDefinitionList(b, l + 1);
    r = r && consumeToken(b, VAR_ASSIGN);
    p = r; // pin = 2
    r = r && ExpressionList(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // ShortVarDeclaration
  //   | IncDecStatement
  //   | (LeftHandExprList AssignmentStatement? /*| SendStatement*/)
  public static boolean SimpleStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SimpleStatement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, SIMPLE_STATEMENT, "<simple statement>");
    r = ShortVarDeclaration(b, l + 1);
    if (!r) r = IncDecStatement(b, l + 1);
    if (!r) r = SimpleStatement_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // LeftHandExprList AssignmentStatement?
  private static boolean SimpleStatement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SimpleStatement_2")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = LeftHandExprList(b, l + 1);
    p = r; // pin = LeftHandExprList
    r = r && SimpleStatement_2_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // '(' (identifier ReceiverTail | ReceiverTail) ')'
  public static boolean Receiver(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Receiver")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, RECEIVER, null);
    r = consumeToken(b, LPAREN);
    p = r; // pin = 1
    r = r && report_error_(b, Receiver_1(b, l + 1));
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // identifier ReceiverTail | ReceiverTail
  private static boolean Receiver_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Receiver_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = Receiver_1_0(b, l + 1);
    if (!r) r = ReceiverTail(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // identifier ReceiverTail
  private static boolean Receiver_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Receiver_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    r = r && ReceiverTail(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // Type ','?
  static boolean ReceiverTail(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ReceiverTail")) return false;
    if (!nextTokenIs(b, "", IDENTIFIER, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = Type(b, l + 1);
    r = r && ReceiverTail_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ','?
  private static boolean ReceiverTail_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ReceiverTail_1")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // identifier
  public static boolean ReferenceExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ReferenceExpression")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, REFERENCE_EXPRESSION, r);
    return r;
  }

  /* ********************************************************** */
  // '==' | '!=' | '<' | '<=' | '>' | '>='
  static boolean RelOp(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RelOp")) return false;
    boolean r;
    r = consumeToken(b, EQ);
    if (!r) r = consumeToken(b, NOT_EQ);
    if (!r) r = consumeToken(b, LESS);
    if (!r) r = consumeToken(b, LESS_OR_EQUAL);
    if (!r) r = consumeToken(b, GREATER);
    if (!r) r = consumeToken(b, GREATER_OR_EQUAL);
    return r;
  }

  /* ********************************************************** */
  // '(' TypeListNoPin ')' | Type | Parameters
  public static boolean Result(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Result")) return false;
    if (!nextTokenIs(b, "<result>", IDENTIFIER, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, RESULT, "<result>");
    r = Result_0(b, l + 1);
    if (!r) r = Type(b, l + 1);
    if (!r) r = Parameters(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '(' TypeListNoPin ')'
  private static boolean Result_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Result_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && TypeListNoPin(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // return ExpressionList?
  public static boolean ReturnStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ReturnStatement")) return false;
    if (!nextTokenIs(b, RETURN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, RETURN_STATEMENT, null);
    r = consumeToken(b, RETURN);
    p = r; // pin = 1
    r = r && ReturnStatement_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ExpressionList?
  private static boolean ReturnStatement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ReturnStatement_1")) return false;
    ExpressionList(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // '{' identifier (',' identifier)* '}'
  public static boolean SelectiveImportList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SelectiveImportList")) return false;
    if (!nextTokenIs(b, LBRACE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SELECTIVE_IMPORT_LIST, null);
    r = consumeTokens(b, 2, LBRACE, IDENTIFIER);
    p = r; // pin = 2
    r = r && report_error_(b, SelectiveImportList_2(b, l + 1));
    r = p && consumeToken(b, RBRACE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (',' identifier)*
  private static boolean SelectiveImportList_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SelectiveImportList_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!SelectiveImportList_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "SelectiveImportList_2", c)) break;
    }
    return true;
  }

  // ',' identifier
  private static boolean SelectiveImportList_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SelectiveImportList_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 2, COMMA, IDENTIFIER);
    exit_section_(b, m, null, r);
    return r;
  }

  // AssignmentStatement?
  private static boolean SimpleStatement_2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SimpleStatement_2_1")) return false;
    AssignmentStatement(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // Parameters Result?
  public static boolean Signature(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Signature")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SIGNATURE, null);
    r = Parameters(b, l + 1);
    p = r; // pin = 1
    r = r && Signature_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Result?
  private static boolean Signature_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Signature_1")) return false;
    Result(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // [SimpleStatement ';'?]
  static boolean SimpleStatementOpt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SimpleStatementOpt")) return false;
    SimpleStatementOpt_0(b, l + 1);
    return true;
  }

  // SimpleStatement ';'?
  private static boolean SimpleStatementOpt_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SimpleStatementOpt_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = SimpleStatement(b, l + 1);
    r = r && SimpleStatementOpt_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ';'?
  private static boolean SimpleStatementOpt_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SimpleStatementOpt_0_1")) return false;
    consumeToken(b, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // mut?
  public static boolean SymbolMutability(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SymbolMutability")) return false;
    Marker m = enter_section_(b, l, _NONE_, SYMBOL_MUTABILITY, "<symbol mutability>");
    consumeToken(b, MUT);
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // FunctionDeclaration
  //   | MethodDeclaration
  static boolean TopDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TopDeclaration")) return false;
    boolean r;
    r = FunctionDeclaration(b, l + 1);
    if (!r) r = MethodDeclaration(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // SymbolMutability identifier
  public static boolean VarDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VarDefinition")) return false;
    if (!nextTokenIs(b, "<var definition>", IDENTIFIER, MUT)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VAR_DEFINITION, "<var definition>");
    r = SymbolMutability(b, l + 1);
    r = r && consumeToken(b, IDENTIFIER);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // SimpleStatement
  // //  | GoStatement
  //   | ReturnStatement
  // //  | BreakStatement
  // //  | ContinueStatement
  // //  | GotoStatement
  // //  | FallthroughStatement
  // //  Block
  //   | IfStatement
  // //  | SwitchStatement
  // //  | SelectStatement
  //   | ForStatement
  public static boolean Statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, STATEMENT, "<statement>");
    r = SimpleStatement(b, l + 1);
    if (!r) r = ReturnStatement(b, l + 1);
    if (!r) r = IfStatement(b, l + 1);
    if (!r) r = ForStatement(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // !('!' | '&' | '(' | '*' | '+' | '-' | ';' | '<-' | '^' | 'type' | '{' | '|' | '|=' | '||' | '}' | break | case | char | const | continue | decimali | default | defer | else | fallthrough | float | floati | for | func | pub | mut | go | goto | hex | identifier | if | int | interface | map | oct | return | select | string | raw_string | struct | switch | var)
  static boolean StatementRecover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StatementRecover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !StatementRecover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '!' | '&' | '(' | '*' | '+' | '-' | ';' | '<-' | '^' | 'type' | '{' | '|' | '|=' | '||' | '}' | break | case | char | const | continue | decimali | default | defer | else | fallthrough | float | floati | for | func | pub | mut | go | goto | hex | identifier | if | int | interface | map | oct | return | select | string | raw_string | struct | switch | var
  private static boolean StatementRecover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StatementRecover_0")) return false;
    boolean r;
    r = consumeToken(b, NOT);
    if (!r) r = consumeToken(b, BIT_AND);
    if (!r) r = consumeToken(b, LPAREN);
    if (!r) r = consumeToken(b, MUL);
    if (!r) r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, MINUS);
    if (!r) r = consumeToken(b, SEMICOLON);
    if (!r) r = consumeToken(b, SEND_CHANNEL);
    if (!r) r = consumeToken(b, BIT_XOR);
    if (!r) r = consumeToken(b, TYPE_);
    if (!r) r = consumeToken(b, LBRACE);
    if (!r) r = consumeToken(b, BIT_OR);
    if (!r) r = consumeToken(b, BIT_OR_ASSIGN);
    if (!r) r = consumeToken(b, COND_OR);
    if (!r) r = consumeToken(b, RBRACE);
    if (!r) r = consumeToken(b, BREAK);
    if (!r) r = consumeToken(b, CASE);
    if (!r) r = consumeToken(b, CHAR);
    if (!r) r = consumeToken(b, CONST);
    if (!r) r = consumeToken(b, CONTINUE);
    if (!r) r = consumeToken(b, DECIMALI);
    if (!r) r = consumeToken(b, DEFAULT);
    if (!r) r = consumeToken(b, DEFER);
    if (!r) r = consumeToken(b, ELSE);
    if (!r) r = consumeToken(b, FALLTHROUGH);
    if (!r) r = consumeToken(b, FLOAT);
    if (!r) r = consumeToken(b, FLOATI);
    if (!r) r = consumeToken(b, FOR);
    if (!r) r = consumeToken(b, FUNC);
    if (!r) r = consumeToken(b, PUB);
    if (!r) r = consumeToken(b, MUT);
    if (!r) r = consumeToken(b, GO);
    if (!r) r = consumeToken(b, GOTO);
    if (!r) r = consumeToken(b, HEX);
    if (!r) r = consumeToken(b, IDENTIFIER);
    if (!r) r = consumeToken(b, IF);
    if (!r) r = consumeToken(b, INT);
    if (!r) r = consumeToken(b, INTERFACE);
    if (!r) r = consumeToken(b, MAP);
    if (!r) r = consumeToken(b, OCT);
    if (!r) r = consumeToken(b, RETURN);
    if (!r) r = consumeToken(b, SELECT);
    if (!r) r = consumeToken(b, STRING);
    if (!r) r = consumeToken(b, RAW_STRING);
    if (!r) r = consumeToken(b, STRUCT);
    if (!r) r = consumeToken(b, SWITCH);
    if (!r) r = consumeToken(b, VAR);
    return r;
  }

  /* ********************************************************** */
  // Statement (semi|&'}')
  static boolean StatementWithSemi(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StatementWithSemi")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = Statement(b, l + 1);
    p = r; // pin = 1
    r = r && StatementWithSemi_1(b, l + 1);
    exit_section_(b, l, m, r, p, VlangParser::StatementRecover);
    return r || p;
  }

  // semi|&'}'
  private static boolean StatementWithSemi_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StatementWithSemi_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = semi(b, l + 1);
    if (!r) r = StatementWithSemi_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // &'}'
  private static boolean StatementWithSemi_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StatementWithSemi_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = consumeToken(b, RBRACE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // StatementWithSemi*
  static boolean Statements(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Statements")) return false;
    while (true) {
      int c = current_position_(b);
      if (!StatementWithSemi(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "Statements", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // string | raw_string
  public static boolean StringLiteral(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StringLiteral")) return false;
    if (!nextTokenIs(b, "<string literal>", RAW_STRING, STRING)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STRING_LITERAL, "<string literal>");
    r = consumeToken(b, STRING);
    if (!r) r = consumeToken(b, RAW_STRING);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // VarDefinition ( ',' VarDefinition )*
  static boolean VarDefinitionList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VarDefinitionList")) return false;
    if (!nextTokenIs(b, "", IDENTIFIER, MUT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = VarDefinition(b, l + 1);
    p = r; // pin = 1
    r = r && VarDefinitionList_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // pub?
  public static boolean SymbolVisibility(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SymbolVisibility")) return false;
    Marker m = enter_section_(b, l, _NONE_, SYMBOL_VISIBILITY, "<symbol visibility>");
    consumeToken(b, PUB);
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  // ( ',' VarDefinition )*
  private static boolean VarDefinitionList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VarDefinitionList_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!VarDefinitionList_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "VarDefinitionList_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // !<<eof>> TopDeclaration semi
  static boolean TopLevelDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TopLevelDeclaration")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = TopLevelDeclaration_0(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, TopDeclaration(b, l + 1));
    r = p && semi(b, l + 1) && r;
    exit_section_(b, l, m, r, p, VlangParser::TopLevelDeclarationRecover);
    return r || p;
  }

  // !<<eof>>
  private static boolean TopLevelDeclaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TopLevelDeclaration_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !eof(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // !(';' |'type' | const | func | pub | var)
  static boolean TopLevelDeclarationRecover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TopLevelDeclarationRecover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !TopLevelDeclarationRecover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ';' |'type' | const | func | pub | var
  private static boolean TopLevelDeclarationRecover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TopLevelDeclarationRecover_0")) return false;
    boolean r;
    r = consumeToken(b, SEMICOLON);
    if (!r) r = consumeToken(b, TYPE_);
    if (!r) r = consumeToken(b, CONST);
    if (!r) r = consumeToken(b, FUNC);
    if (!r) r = consumeToken(b, PUB);
    if (!r) r = consumeToken(b, VAR);
    return r;
  }

  /* ********************************************************** */
  // TypeName
  // //  | TypeLit
  //   | ParType
  public static boolean Type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Type")) return false;
    if (!nextTokenIs(b, "<type>", IDENTIFIER, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, TYPE, "<type>");
    r = TypeName(b, l + 1);
    if (!r) r = ParType(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // Type ( ',' Type )* ','?
  public static boolean TypeList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeList")) return false;
    if (!nextTokenIs(b, "<type list>", IDENTIFIER, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _COLLAPSE_, TYPE_LIST, "<type list>");
    r = Type(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, TypeList_1(b, l + 1));
    r = p && TypeList_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ( ',' Type )*
  private static boolean TypeList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeList_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!TypeList_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "TypeList_1", c)) break;
    }
    return true;
  }

  // ',' Type
  private static boolean TypeList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeList_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && Type(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ','?
  private static boolean TypeList_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeList_2")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // Type ( ',' Type )* ','?
  public static boolean TypeListNoPin(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeListNoPin")) return false;
    if (!nextTokenIs(b, "<type list no pin>", IDENTIFIER, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, TYPE_LIST, "<type list no pin>");
    r = Type(b, l + 1);
    r = r && TypeListNoPin_1(b, l + 1);
    r = r && TypeListNoPin_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ( ',' Type )*
  private static boolean TypeListNoPin_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeListNoPin_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!TypeListNoPin_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "TypeListNoPin_1", c)) break;
    }
    return true;
  }

  // ',' Type
  private static boolean TypeListNoPin_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeListNoPin_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && Type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ','?
  private static boolean TypeListNoPin_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeListNoPin_2")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // TypeReferenceExpression QualifiedTypeReferenceExpression?
  static boolean TypeName(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeName")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = TypeReferenceExpression(b, l + 1);
    r = r && TypeName_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // QualifiedTypeReferenceExpression?
  private static boolean TypeName_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeName_1")) return false;
    QualifiedTypeReferenceExpression(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // identifier
  public static boolean TypeReferenceExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeReferenceExpression")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, TYPE_REFERENCE_EXPRESSION, r);
    return r;
  }

  /* ********************************************************** */
  // '+' | '-' | '!' | '^' | '*' | '&' | '<-'
  static boolean UnaryOp(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "UnaryOp")) return false;
    boolean r;
    r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, MINUS);
    if (!r) r = consumeToken(b, NOT);
    if (!r) r = consumeToken(b, BIT_XOR);
    if (!r) r = consumeToken(b, MUL);
    if (!r) r = consumeToken(b, BIT_AND);
    if (!r) r = consumeToken(b, SEND_CHANNEL);
    return r;
  }

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, EXTENDS_SETS_);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  // ',' VarDefinition
  private static boolean VarDefinitionList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VarDefinitionList_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && VarDefinition(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // '=' | '+=' | '-=' | '|=' | '^=' | '*=' | '/=' | '%=' | '<<=' | '>>=' | '&=' | '&^='
  public static boolean assign_op(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assign_op")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ASSIGN_OP, "<assign op>");
    r = consumeToken(b, ASSIGN);
    if (!r) r = consumeToken(b, PLUS_ASSIGN);
    if (!r) r = consumeToken(b, MINUS_ASSIGN);
    if (!r) r = consumeToken(b, BIT_OR_ASSIGN);
    if (!r) r = consumeToken(b, BIT_XOR_ASSIGN);
    if (!r) r = consumeToken(b, MUL_ASSIGN);
    if (!r) r = consumeToken(b, QUOTIENT_ASSIGN);
    if (!r) r = consumeToken(b, REMAINDER_ASSIGN);
    if (!r) r = consumeToken(b, SHIFT_LEFT_ASSIGN);
    if (!r) r = consumeToken(b, SHIFT_RIGHT_ASSIGN);
    if (!r) r = consumeToken(b, BIT_AND_ASSIGN);
    if (!r) r = consumeToken(b, BIT_CLEAR_ASSIGN);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '<NL>' | ';' | <<eof>>
  static boolean semi(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "semi")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SEMICOLON_SYNTHETIC);
    if (!r) r = consumeToken(b, SEMICOLON);
    if (!r) r = eof(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // Expression root: Expression
  // Operator priority table:
  // 0: BINARY(OrExpr)
  // 1: BINARY(AndExpr)
  // 2: BINARY(ConditionalExpr)
  // 3: BINARY(AddExpr)
  // 4: BINARY(MulExpr)
  // 5: PREFIX(UnaryExpr)
  // 6: ATOM(OperandName) POSTFIX(CallExpr) ATOM(Literal)
  // 7: ATOM(ParenthesesExpr)
  public static boolean Expression(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "Expression")) return false;
    addVariant(b, "<expression>");
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<expression>");
    r = UnaryExpr(b, l + 1);
    if (!r) r = OperandName(b, l + 1);
    if (!r) r = Literal(b, l + 1);
    if (!r) r = ParenthesesExpr(b, l + 1);
    p = r;
    r = r && Expression_0(b, l + 1, g);
    exit_section_(b, l, m, null, r, p, null);
    return r || p;
  }

  public static boolean Expression_0(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "Expression_0")) return false;
    boolean r = true;
    while (true) {
      Marker m = enter_section_(b, l, _LEFT_, null);
      if (g < 0 && consumeTokenSmart(b, COND_OR)) {
        r = Expression(b, l, 0);
        exit_section_(b, l, m, OR_EXPR, r, true, null);
      }
      else if (g < 1 && consumeTokenSmart(b, COND_AND)) {
        r = Expression(b, l, 1);
        exit_section_(b, l, m, AND_EXPR, r, true, null);
      }
      else if (g < 2 && RelOp(b, l + 1)) {
        r = Expression(b, l, 2);
        exit_section_(b, l, m, CONDITIONAL_EXPR, r, true, null);
      }
      else if (g < 3 && AddOp(b, l + 1)) {
        r = Expression(b, l, 3);
        exit_section_(b, l, m, ADD_EXPR, r, true, null);
      }
      else if (g < 4 && MulOp(b, l + 1)) {
        r = Expression(b, l, 4);
        exit_section_(b, l, m, MUL_EXPR, r, true, null);
      }
      else if (g < 6 && ArgumentList(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, CALL_EXPR, r, true, null);
      }
      else {
        exit_section_(b, l, m, null, false, false, null);
        break;
      }
    }
    return r;
  }

  public static boolean UnaryExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "UnaryExpr")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = UnaryOp(b, l + 1);
    p = r;
    r = p && Expression(b, l, 5);
    exit_section_(b, l, m, UNARY_EXPR, r, p, null);
    return r || p;
  }

  // ReferenceExpression QualifiedReferenceExpression?
  public static boolean OperandName(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "OperandName")) return false;
    if (!nextTokenIsSmart(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, REFERENCE_EXPRESSION, null);
    r = ReferenceExpression(b, l + 1);
    r = r && OperandName_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // QualifiedReferenceExpression?
  private static boolean OperandName_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "OperandName_1")) return false;
    QualifiedReferenceExpression(b, l + 1);
    return true;
  }

  // int
  //   | float
  //   | floati
  //   | decimali
  //   | hex
  //   | oct
  //   | StringLiteral
  //   | char
  public static boolean Literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Literal")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, LITERAL, "<literal>");
    r = consumeTokenSmart(b, INT);
    if (!r) r = consumeTokenSmart(b, FLOAT);
    if (!r) r = consumeTokenSmart(b, FLOATI);
    if (!r) r = consumeTokenSmart(b, DECIMALI);
    if (!r) r = consumeTokenSmart(b, HEX);
    if (!r) r = consumeTokenSmart(b, OCT);
    if (!r) r = StringLiteral(b, l + 1);
    if (!r) r = consumeTokenSmart(b, CHAR);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '(' /*<<enterMode "PAR">>*/ Expression /*<<exitModeSafe "PAR">>*/')'
  public static boolean ParenthesesExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParenthesesExpr")) return false;
    if (!nextTokenIsSmart(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PARENTHESES_EXPR, null);
    r = consumeTokenSmart(b, LPAREN);
    p = r; // pin = 1
    r = r && report_error_(b, Expression(b, l + 1, -1));
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

}
