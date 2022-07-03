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

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[] {
    create_token_set_(RANGE_CLAUSE, SHORT_VAR_DECLARATION),
    create_token_set_(ARRAY_OR_SLICE_TYPE, NULLABLE_TYPE, PAR_TYPE, POINTER_TYPE,
      STRUCT_TYPE, TYPE, TYPE_LIST),
    create_token_set_(ASSERT_STATEMENT, ASSIGNMENT_STATEMENT, BREAK_STATEMENT, COMPILE_ELSE_STATEMENT,
      COMPILE_TIME_IF_STATEMENT, CONTINUE_STATEMENT, DEFER_STATEMENT, ELSE_STATEMENT,
      FOR_STATEMENT, GO_STATEMENT, IF_STATEMENT, INC_DEC_STATEMENT,
      RETURN_STATEMENT, SIMPLE_STATEMENT, STATEMENT, UNSAFE_STATEMENT),
    create_token_set_(ADD_EXPR, AND_EXPR, ARRAY_CREATION, CALL_EXPR,
      COMPILE_TIME_IF_EXPRESSION, CONDITIONAL_EXPR, EXPRESSION, IF_EXPRESSION,
      INDEX_OR_SLICE_EXPR, LITERAL, MUL_EXPR, OR_EXPR,
      PARENTHESES_EXPR, RANGE_EXPR, REFERENCE_EXPRESSION, STRING_LITERAL,
      STRUCT_INITIALIZATION, UNARY_EXPR, UNSAFE_EXPRESSION),
  };

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return File(b, l + 1);
  }

  /* ********************************************************** */
  // Type
  public static boolean AnonymousFieldDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AnonymousFieldDefinition")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ANONYMOUS_FIELD_DEFINITION, "<anonymous field definition>");
    r = Type(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ExpressionWithRecover (',' (ExpressionWithRecover | &']'))*
  public static boolean ArrayCreationList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArrayCreationList")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ARRAY_CREATION_LIST, "<array creation list>");
    r = ExpressionWithRecover(b, l + 1);
    p = r; // pin = 1
    r = r && ArrayCreationList_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (',' (ExpressionWithRecover | &']'))*
  private static boolean ArrayCreationList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArrayCreationList_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ArrayCreationList_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ArrayCreationList_1", c)) break;
    }
    return true;
  }

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

  // ',' (ExpressionWithRecover | &']')
  private static boolean ArrayCreationList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArrayCreationList_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && ArrayCreationList_1_0_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
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

  // ExpressionWithRecover | &']'
  private static boolean ArrayCreationList_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArrayCreationList_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ExpressionWithRecover(b, l + 1);
    if (!r) r = ArrayCreationList_1_0_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // &']'
  private static boolean ArrayCreationList_1_0_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArrayCreationList_1_0_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = consumeToken(b, RBRACK);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '[' ('...'|Expression?) ']' Type
  public static boolean ArrayOrSliceType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArrayOrSliceType")) return false;
    if (!nextTokenIs(b, LBRACK)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ARRAY_OR_SLICE_TYPE, null);
    r = consumeToken(b, LBRACK);
    p = r; // pin = 1
    r = r && report_error_(b, ArrayOrSliceType_1(b, l + 1));
    r = p && report_error_(b, consumeToken(b, RBRACK)) && r;
    r = p && Type(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // '...'|Expression?
  private static boolean ArrayOrSliceType_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArrayOrSliceType_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TRIPLE_DOT);
    if (!r) r = ArrayOrSliceType_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // Expression?
  private static boolean ArrayOrSliceType_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArrayOrSliceType_1_1")) return false;
    Expression(b, l + 1, -1);
    return true;
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
  // assert Expression
  public static boolean AssertStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AssertStatement")) return false;
    if (!nextTokenIs(b, ASSERT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ASSERT);
    r = r && Expression(b, l + 1, -1);
    exit_section_(b, m, ASSERT_STATEMENT, r);
    return r;
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

  /* ********************************************************** */
  // '[' AttributeExpression (';' AttributeExpression)* ']'
  public static boolean Attribute(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Attribute")) return false;
    if (!nextTokenIs(b, LBRACK)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ATTRIBUTE, null);
    r = consumeToken(b, LBRACK);
    p = r; // pin = 1
    r = r && report_error_(b, AttributeExpression(b, l + 1));
    r = p && report_error_(b, Attribute_2(b, l + 1)) && r;
    r = p && consumeToken(b, RBRACK) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (';' AttributeExpression)*
  private static boolean Attribute_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Attribute_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Attribute_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "Attribute_2", c)) break;
    }
    return true;
  }

  // ';' AttributeExpression
  private static boolean Attribute_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Attribute_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SEMICOLON);
    r = r && AttributeExpression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // IfAttribute | PlainAttribute
  public static boolean AttributeExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AttributeExpression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ATTRIBUTE_EXPRESSION, "<attribute expression>");
    r = IfAttribute(b, l + 1);
    if (!r) r = PlainAttribute(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // Attribute (semi Attribute)* semi?
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
  // ELSE_COMPILE_TIME (CompileTimeIfStatement | Block)
  public static boolean CompileElseStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CompileElseStatement")) return false;
    if (!nextTokenIs(b, ELSE_COMPILE_TIME)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, COMPILE_ELSE_STATEMENT, null);
    r = consumeToken(b, ELSE_COMPILE_TIME);
    p = r; // pin = 1
    r = r && CompileElseStatement_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // semi?
  private static boolean Attributes_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Attributes_2")) return false;
    semi(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // CompileTimeIfExpression
  public static boolean CompileTimeIfStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CompileTimeIfStatement")) return false;
    if (!nextTokenIs(b, IF_COMPILE_TIME)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = CompileTimeIfExpression(b, l + 1);
    exit_section_(b, m, COMPILE_TIME_IF_STATEMENT, r);
    return r;
  }

  /* ********************************************************** */
  // '{' ('}' | Statements '}')
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

  // '}' | Statements '}'
  private static boolean BlockInner_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BlockInner_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, RBRACE);
    if (!r) r = BlockInner_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // Statements '}'
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

  // Expression?
  private static boolean Condition_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Condition_1")) return false;
    Expression(b, l + 1, -1);
    return true;
  }

  /* ********************************************************** */
  // break LabelRef?
  public static boolean BreakStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BreakStatement")) return false;
    if (!nextTokenIs(b, BREAK)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BREAK);
    r = r && BreakStatement_1(b, l + 1);
    exit_section_(b, m, BREAK_STATEMENT, r);
    return r;
  }

  // LabelRef?
  private static boolean BreakStatement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BreakStatement_1")) return false;
    LabelRef(b, l + 1);
    return true;
  }

  // ErrorPropagation?
  private static boolean Condition_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Condition_2")) return false;
    ErrorPropagation(b, l + 1);
    return true;
  }

  // CompileTimeIfStatement | Block
  private static boolean CompileElseStatement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CompileElseStatement_1")) return false;
    boolean r;
    r = CompileTimeIfStatement(b, l + 1);
    if (!r) r = Block(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // const ( ConstSpec | '(' ConstSpecs? ')' )
  public static boolean ConstDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstDeclaration")) return false;
    if (!nextTokenIs(b, CONST)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CONST_DECLARATION, null);
    r = consumeToken(b, CONST);
    p = r; // pin = 1
    r = r && ConstDeclaration_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // SimpleStatementOpt Expression? ErrorPropagation?
  static boolean Condition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Condition")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = SimpleStatementOpt(b, l + 1);
    r = r && Condition_1(b, l + 1);
    r = r && Condition_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ConstSpec | '(' ConstSpecs? ')'
  private static boolean ConstDeclaration_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstDeclaration_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ConstSpec(b, l + 1);
    if (!r) r = ConstDeclaration_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '=' Expression
  public static boolean DefaultFieldValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "DefaultFieldValue")) return false;
    if (!nextTokenIs(b, ASSIGN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ASSIGN);
    r = r && Expression(b, l + 1, -1);
    exit_section_(b, m, DEFAULT_FIELD_VALUE, r);
    return r;
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

  /* ********************************************************** */
  // '?'
  public static boolean ErrorPropagation(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ErrorPropagation")) return false;
    if (!nextTokenIs(b, QUESTION)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, QUESTION);
    exit_section_(b, m, ERROR_PROPAGATION, r);
    return r;
  }

  // '(' ConstSpecs? ')'
  private static boolean ConstDeclaration_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstDeclaration_1_1")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, LPAREN);
    p = r; // pin = 1
    r = r && report_error_(b, ConstDeclaration_1_1_1(b, l + 1));
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ConstSpecs?
  private static boolean ConstDeclaration_1_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstDeclaration_1_1_1")) return false;
    ConstSpecs(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // identifier
  public static boolean ConstDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstDefinition")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, CONST_DEFINITION, r);
    return r;
  }

  /* ********************************************************** */
  // ConstDefinition ( ',' ConstDefinition )*
  static boolean ConstDefinitionList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstDefinitionList")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = ConstDefinition(b, l + 1);
    p = r; // pin = 1
    r = r && ConstDefinitionList_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ( ',' ConstDefinition )*
  private static boolean ConstDefinitionList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstDefinitionList_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ConstDefinitionList_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ConstDefinitionList_1", c)) break;
    }
    return true;
  }

  // ',' ConstDefinition
  private static boolean ConstDefinitionList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstDefinitionList_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && ConstDefinition(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // ConstDefinitionList [ ('=' ExpressionList | Type '=' ExpressionList) ]
  public static boolean ConstSpec(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstSpec")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CONST_SPEC, null);
    r = ConstDefinitionList(b, l + 1);
    p = r; // pin = 1
    r = r && ConstSpec_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ ('=' ExpressionList | Type '=' ExpressionList) ]
  private static boolean ConstSpec_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstSpec_1")) return false;
    ConstSpec_1_0(b, l + 1);
    return true;
  }

  // '=' ExpressionList | Type '=' ExpressionList
  private static boolean ConstSpec_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstSpec_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ConstSpec_1_0_0(b, l + 1);
    if (!r) r = ConstSpec_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '=' ExpressionList
  private static boolean ConstSpec_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstSpec_1_0_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, ASSIGN);
    p = r; // pin = 1
    r = r && ExpressionList(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Type '=' ExpressionList
  private static boolean ConstSpec_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstSpec_1_0_1")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = Type(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, consumeToken(b, ASSIGN));
    r = p && ExpressionList(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // ConstSpec (semi ConstSpec)* semi?
  static boolean ConstSpecs(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstSpecs")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = ConstSpec(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, ConstSpecs_1(b, l + 1));
    r = p && ConstSpecs_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (semi ConstSpec)*
  private static boolean ConstSpecs_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstSpecs_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ConstSpecs_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ConstSpecs_1", c)) break;
    }
    return true;
  }

  // semi ConstSpec
  private static boolean ConstSpecs_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstSpecs_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = semi(b, l + 1);
    r = r && ConstSpec(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // semi?
  private static boolean ConstSpecs_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstSpecs_2")) return false;
    semi(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // continue LabelRef?
  public static boolean ContinueStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ContinueStatement")) return false;
    if (!nextTokenIs(b, CONTINUE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CONTINUE);
    r = r && ContinueStatement_1(b, l + 1);
    exit_section_(b, m, CONTINUE_STATEMENT, r);
    return r;
  }

  // LabelRef?
  private static boolean ContinueStatement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ContinueStatement_1")) return false;
    LabelRef(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // !('!' | '?' | '!=' | '%' | '%=' | '&&' | '&' | '&=' | '&^' | '&^=' | '(' | ')' | '*' | '*=' | '+' | '++' | '+=' | ',' | '-' | '--' | '-=' | '...' | '/' | '/=' | ':' | ';' | '<' | '<-' | '<<' | '<<=' | '<=' | '<NL>' | '=' | '==' | '>' | '>=' | '>>' | '>>=' | '[' | ']' | '^' | '^=' | 'type' | '{' | '|' | '|=' | '||' | '}' | break | case | chan | char | const | continue | decimali | default | defer | else | fallthrough | float | floati | for | fn | go | goto | hex | identifier | if | int | interface | oct | return | select | string | raw_string | struct | switch | var | unsafe | assert)
  static boolean ExpressionListRecover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ExpressionListRecover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !ExpressionListRecover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // defer Block
  public static boolean DeferStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "DeferStatement")) return false;
    if (!nextTokenIs(b, DEFER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DEFER);
    r = r && Block(b, l + 1);
    exit_section_(b, m, DEFER_STATEMENT, r);
    return r;
  }

  // '!' | '?' | '!=' | '%' | '%=' | '&&' | '&' | '&=' | '&^' | '&^=' | '(' | ')' | '*' | '*=' | '+' | '++' | '+=' | ',' | '-' | '--' | '-=' | '...' | '/' | '/=' | ':' | ';' | '<' | '<-' | '<<' | '<<=' | '<=' | '<NL>' | '=' | '==' | '>' | '>=' | '>>' | '>>=' | '[' | ']' | '^' | '^=' | 'type' | '{' | '|' | '|=' | '||' | '}' | break | case | chan | char | const | continue | decimali | default | defer | else | fallthrough | float | floati | for | fn | go | goto | hex | identifier | if | int | interface | oct | return | select | string | raw_string | struct | switch | var | unsafe | assert
  private static boolean ExpressionListRecover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ExpressionListRecover_0")) return false;
    boolean r;
    r = consumeToken(b, NOT);
    if (!r) r = consumeToken(b, QUESTION);
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
    if (!r) r = consumeToken(b, FN);
    if (!r) r = consumeToken(b, GO);
    if (!r) r = consumeToken(b, GOTO);
    if (!r) r = consumeToken(b, HEX);
    if (!r) r = consumeToken(b, IDENTIFIER);
    if (!r) r = consumeToken(b, IF);
    if (!r) r = consumeToken(b, INT);
    if (!r) r = consumeToken(b, INTERFACE);
    if (!r) r = consumeToken(b, OCT);
    if (!r) r = consumeToken(b, RETURN);
    if (!r) r = consumeToken(b, SELECT);
    if (!r) r = consumeToken(b, STRING);
    if (!r) r = consumeToken(b, RAW_STRING);
    if (!r) r = consumeToken(b, STRUCT);
    if (!r) r = consumeToken(b, SWITCH);
    if (!r) r = consumeToken(b, VAR);
    if (!r) r = consumeToken(b, UNSAFE);
    if (!r) r = consumeToken(b, ASSERT);
    return r;
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
  // (FieldDefinitionList Type | AnonymousFieldDefinition) DefaultFieldValue? Attribute? Tag?
  public static boolean FieldDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldDeclaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FIELD_DECLARATION, "<field declaration>");
    r = FieldDeclaration_0(b, l + 1);
    r = r && FieldDeclaration_1(b, l + 1);
    r = r && FieldDeclaration_2(b, l + 1);
    r = r && FieldDeclaration_3(b, l + 1);
    exit_section_(b, l, m, r, false, null);
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

  // FieldDefinitionList Type | AnonymousFieldDefinition
  private static boolean FieldDeclaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldDeclaration_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = FieldDeclaration_0_0(b, l + 1);
    if (!r) r = AnonymousFieldDefinition(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // FieldDefinitionList Type
  private static boolean FieldDeclaration_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldDeclaration_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = FieldDefinitionList(b, l + 1);
    r = r && Type(b, l + 1);
    exit_section_(b, m, null, r);
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

  /* ********************************************************** */
  // FieldInitializationKeyValueList
  public static boolean FieldInitialization(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldInitialization")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FIELD_INITIALIZATION, "<field initialization>");
    r = FieldInitializationKeyValueList(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // Expression ':' Expression
  static boolean FieldInitializationKeyValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldInitializationKeyValue")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = Expression(b, l + 1, -1);
    r = r && consumeToken(b, COLON);
    p = r; // pin = 2
    r = r && Expression(b, l + 1, -1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // (FieldInitializationKeyValue semi)+ semi?
  public static boolean FieldInitializationKeyValueList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldInitializationKeyValueList")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FIELD_INITIALIZATION_KEY_VALUE_LIST, "<field initialization key value list>");
    r = FieldInitializationKeyValueList_0(b, l + 1);
    p = r; // pin = 1
    r = r && FieldInitializationKeyValueList_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // DefaultFieldValue?
  private static boolean FieldDeclaration_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldDeclaration_1")) return false;
    DefaultFieldValue(b, l + 1);
    return true;
  }

  // Attribute?
  private static boolean FieldDeclaration_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldDeclaration_2")) return false;
    Attribute(b, l + 1);
    return true;
  }

  // Tag?
  private static boolean FieldDeclaration_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldDeclaration_3")) return false;
    Tag(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // identifier {
  // //  stubClass="org.vlang.lang.stubs.GoFieldDefinitionStub"
  // }
  public static boolean FieldDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldDefinition")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    r = r && FieldDefinition_1(b, l + 1);
    exit_section_(b, m, FIELD_DEFINITION, r);
    return r;
  }

  // {
  // //  stubClass="org.vlang.lang.stubs.GoFieldDefinitionStub"
  // }
  private static boolean FieldDefinition_1(PsiBuilder b, int l) {
    return true;
  }

  /* ********************************************************** */
  // FieldDefinition (',' FieldDefinition)*
  static boolean FieldDefinitionList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldDefinitionList")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = FieldDefinition(b, l + 1);
    p = r; // pin = 1
    r = r && FieldDefinitionList_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (',' FieldDefinition)*
  private static boolean FieldDefinitionList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldDefinitionList_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!FieldDefinitionList_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "FieldDefinitionList_1", c)) break;
    }
    return true;
  }

  // ',' FieldDefinition
  private static boolean FieldDefinitionList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldDefinitionList_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && FieldDefinition(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // Attributes? SymbolVisibility fn FunctionName Signature BlockWithConsume?
  public static boolean FunctionDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FunctionDeclaration")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_DECLARATION, "<function declaration>");
    r = FunctionDeclaration_0(b, l + 1);
    r = r && SymbolVisibility(b, l + 1);
    r = r && consumeToken(b, FN);
    r = r && FunctionName(b, l + 1);
    p = r; // pin = 4
    r = r && report_error_(b, Signature(b, l + 1));
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

  // (FieldInitializationKeyValue semi)+
  private static boolean FieldInitializationKeyValueList_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldInitializationKeyValueList_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = FieldInitializationKeyValueList_0_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!FieldInitializationKeyValueList_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "FieldInitializationKeyValueList_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // FieldInitializationKeyValue semi
  private static boolean FieldInitializationKeyValueList_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldInitializationKeyValueList_0_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = FieldInitializationKeyValue(b, l + 1);
    p = r; // pin = 1
    r = r && semi(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // semi?
  private static boolean FieldInitializationKeyValueList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldInitializationKeyValueList_1")) return false;
    semi(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // mut | pub | BUILTIN_GLOBAL
  public static boolean FieldModifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldModifier")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FIELD_MODIFIER, "<field modifier>");
    r = consumeToken(b, MUT);
    if (!r) r = consumeToken(b, PUB);
    if (!r) r = consumeToken(b, BUILTIN_GLOBAL);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // FieldsModifiers? FieldDeclaration? (semi Fields)* semi?
  static boolean Fields(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Fields")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = Fields_0(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, Fields_1(b, l + 1));
    r = p && report_error_(b, Fields_2(b, l + 1)) && r;
    r = p && Fields_3(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // FieldsModifiers?
  private static boolean Fields_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Fields_0")) return false;
    FieldsModifiers(b, l + 1);
    return true;
  }

  // FieldDeclaration?
  private static boolean Fields_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Fields_1")) return false;
    FieldDeclaration(b, l + 1);
    return true;
  }

  // (semi Fields)*
  private static boolean Fields_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Fields_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Fields_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "Fields_2", c)) break;
    }
    return true;
  }

  // semi Fields
  private static boolean Fields_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Fields_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = semi(b, l + 1);
    r = r && Fields(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // semi?
  private static boolean Fields_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Fields_3")) return false;
    semi(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // FieldModifier (FieldModifier)* ':'
  public static boolean FieldsModifiers(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldsModifiers")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FIELDS_MODIFIERS, "<fields modifiers>");
    r = FieldModifier(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, FieldsModifiers_1(b, l + 1));
    r = p && consumeToken(b, COLON) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (FieldModifier)*
  private static boolean FieldsModifiers_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldsModifiers_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!FieldsModifiers_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "FieldsModifiers_1", c)) break;
    }
    return true;
  }

  // (FieldModifier)
  private static boolean FieldsModifiers_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldsModifiers_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = FieldModifier(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ModuleClause? ImportList? TopLevelDeclaration*
  static boolean File(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "File")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = File_0(b, l + 1);
    r = r && File_1(b, l + 1);
    r = r && File_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ModuleClause?
  private static boolean File_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "File_0")) return false;
    ModuleClause(b, l + 1);
    return true;
  }

  // ImportList?
  private static boolean File_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "File_1")) return false;
    ImportList(b, l + 1);
    return true;
  }

  // TopLevelDeclaration*
  private static boolean File_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "File_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!TopLevelDeclaration(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "File_2", c)) break;
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
  // IfExpression
  public static boolean IfStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IfStatement")) return false;
    if (!nextTokenIs(b, IF)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = IfExpression(b, l + 1);
    exit_section_(b, m, IF_STATEMENT, r);
    return r;
  }

  /* ********************************************************** */
  // (ImportDeclaration semi)+
  public static boolean ImportList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ImportList")) return false;
    if (!nextTokenIs(b, IMPORT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ImportList_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!ImportList_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ImportList", c)) break;
    }
    exit_section_(b, m, IMPORT_LIST, r);
    return r;
  }

  /* ********************************************************** */
  // Expression SliceExprBodyInner?
  static boolean IndexExprBody(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IndexExprBody")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = Expression(b, l + 1, -1);
    r = r && IndexExprBody_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // FunctionNamePart ('.' FunctionNamePart)*
  static boolean FunctionName(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FunctionName")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = FunctionNamePart(b, l + 1);
    r = r && FunctionName_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ('.' FunctionNamePart)*
  private static boolean FunctionName_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FunctionName_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!FunctionName_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "FunctionName_1", c)) break;
    }
    return true;
  }

  // '.' FunctionNamePart
  private static boolean FunctionName_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FunctionName_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DOT);
    r = r && FunctionNamePart(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // identifier
  public static boolean FunctionNamePart(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FunctionNamePart")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, FUNCTION_NAME_PART, r);
    return r;
  }

  /* ********************************************************** */
  // Attributes? BUILTIN_GLOBAL identifier '=' Expression
  public static boolean GlobalVariableDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GlobalVariableDeclaration")) return false;
    if (!nextTokenIs(b, "<global variable declaration>", BUILTIN_GLOBAL, LBRACK)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, GLOBAL_VARIABLE_DECLARATION, "<global variable declaration>");
    r = GlobalVariableDeclaration_0(b, l + 1);
    r = r && consumeTokens(b, 2, BUILTIN_GLOBAL, IDENTIFIER, ASSIGN);
    p = r; // pin = 3
    r = r && Expression(b, l + 1, -1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Attributes?
  private static boolean GlobalVariableDeclaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GlobalVariableDeclaration_0")) return false;
    Attributes(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // go Expression
  public static boolean GoStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GoStatement")) return false;
    if (!nextTokenIs(b, GO)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, GO);
    r = r && Expression(b, l + 1, -1);
    exit_section_(b, m, GO_STATEMENT, r);
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

  // SliceExprBodyInner?
  private static boolean IndexExprBody_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IndexExprBody_1")) return false;
    SliceExprBodyInner(b, l + 1);
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
  // identifier
  public static boolean LabelRef(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "LabelRef")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, LABEL_REF, r);
    return r;
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
  // (identifier | unsafe) (':' StringLiteral)?
  public static boolean PlainAttribute(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PlainAttribute")) return false;
    if (!nextTokenIs(b, "<plain attribute>", IDENTIFIER, UNSAFE)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PLAIN_ATTRIBUTE, "<plain attribute>");
    r = PlainAttribute_0(b, l + 1);
    r = r && PlainAttribute_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
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

  /* ********************************************************** */
  // Attributes? SymbolVisibility fn Receiver identifier Signature BlockWithConsume?
  public static boolean MethodDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MethodDeclaration")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, METHOD_DECLARATION, "<method declaration>");
    r = MethodDeclaration_0(b, l + 1);
    r = r && SymbolVisibility(b, l + 1);
    r = r && consumeToken(b, FN);
    r = r && Receiver(b, l + 1);
    r = r && consumeToken(b, IDENTIFIER);
    p = r; // pin = 5
    r = r && report_error_(b, Signature(b, l + 1));
    r = p && MethodDeclaration_6(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Attributes?
  private static boolean MethodDeclaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MethodDeclaration_0")) return false;
    Attributes(b, l + 1);
    return true;
  }

  // BlockWithConsume?
  private static boolean MethodDeclaration_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MethodDeclaration_6")) return false;
    BlockWithConsume(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // Attributes? module identifier semi
  public static boolean ModuleClause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ModuleClause")) return false;
    if (!nextTokenIs(b, "<module clause>", LBRACK, MODULE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, MODULE_CLAUSE, "<module clause>");
    r = ModuleClause_0(b, l + 1);
    r = r && consumeTokens(b, 1, MODULE, IDENTIFIER);
    p = r; // pin = 2
    r = r && semi(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Attributes?
  private static boolean ModuleClause_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ModuleClause_0")) return false;
    Attributes(b, l + 1);
    return true;
  }

  // identifier | unsafe
  private static boolean PlainAttribute_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PlainAttribute_0")) return false;
    boolean r;
    r = consumeToken(b, IDENTIFIER);
    if (!r) r = consumeToken(b, UNSAFE);
    return r;
  }

  /* ********************************************************** */
  // '?' Type
  public static boolean NullableType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "NullableType")) return false;
    if (!nextTokenIs(b, QUESTION)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, NULLABLE_TYPE, null);
    r = consumeToken(b, QUESTION);
    p = r; // pin = 1
    r = r && Type(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (':' StringLiteral)?
  private static boolean PlainAttribute_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PlainAttribute_1")) return false;
    PlainAttribute_1_0(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // SymbolMutability identifier
  public static boolean ParamDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParamDefinition")) return false;
    if (!nextTokenIs(b, "<param definition>", IDENTIFIER, MUT)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PARAM_DEFINITION, "<param definition>");
    r = SymbolMutability(b, l + 1);
    r = r && consumeToken(b, IDENTIFIER);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ParamDefinition &(!('.' | ')')) (',' ParamDefinition)*
  static boolean ParamDefinitionListNoPin(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParamDefinitionListNoPin")) return false;
    if (!nextTokenIs(b, "", IDENTIFIER, MUT)) return false;
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
  // '&' Type
  public static boolean PointerType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PointerType")) return false;
    if (!nextTokenIs(b, BIT_AND)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, POINTER_TYPE, null);
    r = consumeToken(b, BIT_AND);
    p = r; // pin = 1
    r = r && Type(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // '(' (SymbolMutability identifier ReceiverTail | ReceiverTail) ')'
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

  // SymbolMutability identifier ReceiverTail | ReceiverTail
  private static boolean Receiver_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Receiver_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = Receiver_1_0(b, l + 1);
    if (!r) r = ReceiverTail(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
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

  // SymbolMutability identifier ReceiverTail
  private static boolean Receiver_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Receiver_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = SymbolMutability(b, l + 1);
    r = r && consumeToken(b, IDENTIFIER);
    r = r && ReceiverTail(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
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
  // return ExpressionList?
  public static boolean ReturnStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ReturnStatement")) return false;
    if (!nextTokenIs(b, RETURN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, RETURN);
    r = r && ReturnStatement_1(b, l + 1);
    exit_section_(b, m, RETURN_STATEMENT, r);
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

  /* ********************************************************** */
  // Type ','?
  static boolean ReceiverTail(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ReceiverTail")) return false;
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
  // SymbolVisibility? StructType
  public static boolean StructDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StructDeclaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STRUCT_DECLARATION, "<struct declaration>");
    r = StructDeclaration_0(b, l + 1);
    r = r && StructType(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // SymbolVisibility?
  private static boolean StructDeclaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StructDeclaration_0")) return false;
    SymbolVisibility(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // struct identifier? '{' Fields? '}'
  public static boolean StructType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StructType")) return false;
    if (!nextTokenIs(b, STRUCT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, STRUCT_TYPE, null);
    r = consumeToken(b, STRUCT);
    p = r; // pin = 1
    r = r && report_error_(b, StructType_1(b, l + 1));
    r = p && report_error_(b, consumeToken(b, LBRACE)) && r;
    r = p && report_error_(b, StructType_3(b, l + 1)) && r;
    r = p && consumeToken(b, RBRACE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // &':' (':' Expression ':' Expression) | (':' Expression?)
  static boolean SliceExprBody(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SliceExprBody")) return false;
    if (!nextTokenIs(b, COLON)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = SliceExprBody_0(b, l + 1);
    if (!r) r = SliceExprBody_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // &':' (':' Expression ':' Expression)
  private static boolean SliceExprBody_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SliceExprBody_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = SliceExprBody_0_0(b, l + 1);
    r = r && SliceExprBody_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // &':'
  private static boolean SliceExprBody_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SliceExprBody_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = consumeToken(b, COLON);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ':' Expression ':' Expression
  private static boolean SliceExprBody_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SliceExprBody_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && Expression(b, l + 1, -1);
    r = r && consumeToken(b, COLON);
    r = r && Expression(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ':' Expression?
  private static boolean SliceExprBody_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SliceExprBody_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && SliceExprBody_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // Expression?
  private static boolean SliceExprBody_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SliceExprBody_1_1")) return false;
    Expression(b, l + 1, -1);
    return true;
  }

  /* ********************************************************** */
  // SliceExprBody
  static boolean SliceExprBodyInner(PsiBuilder b, int l) {
    return SliceExprBody(b, l + 1);
  }

  /* ********************************************************** */
  // ConstDeclaration
  // //  | TypeDeclaration
  // //  VarDeclaration
  // //  | LabeledStatement
  //   | SimpleStatement
  //   | GoStatement
  //   | ReturnStatement
  //   | BreakStatement
  //   | ContinueStatement
  // //  | GotoStatement
  // //  | FallthroughStatement
  //   | Block
  //   | CompileTimeIfStatement
  //   | IfStatement
  //   | UnsafeStatement
  // //  | SwitchStatement
  // //  | SelectStatement
  //   | ForStatement
  //   | AssertStatement
  //   | DeferStatement
  public static boolean Statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, STATEMENT, "<statement>");
    r = ConstDeclaration(b, l + 1);
    if (!r) r = SimpleStatement(b, l + 1);
    if (!r) r = GoStatement(b, l + 1);
    if (!r) r = ReturnStatement(b, l + 1);
    if (!r) r = BreakStatement(b, l + 1);
    if (!r) r = ContinueStatement(b, l + 1);
    if (!r) r = Block(b, l + 1);
    if (!r) r = CompileTimeIfStatement(b, l + 1);
    if (!r) r = IfStatement(b, l + 1);
    if (!r) r = UnsafeStatement(b, l + 1);
    if (!r) r = ForStatement(b, l + 1);
    if (!r) r = AssertStatement(b, l + 1);
    if (!r) r = DeferStatement(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // !('!' | '?' | '&' | '(' | '*' | '+' | '-' | ';' | '<-' | '^' | 'type' | '{' | '|' | '|=' | '||' | '}' | break | case | char | const | continue | decimali | default | defer | else | fallthrough | float | floati | for | fn | pub | mut | go | goto | hex | identifier | if | int | interface | oct | return | select | string | raw_string | struct | switch | var | unsafe | assert | IF_COMPILE_TIME | ELSE_COMPILE_TIME | BUILTIN_GLOBAL)
  static boolean StatementRecover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StatementRecover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !StatementRecover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '!' | '?' | '&' | '(' | '*' | '+' | '-' | ';' | '<-' | '^' | 'type' | '{' | '|' | '|=' | '||' | '}' | break | case | char | const | continue | decimali | default | defer | else | fallthrough | float | floati | for | fn | pub | mut | go | goto | hex | identifier | if | int | interface | oct | return | select | string | raw_string | struct | switch | var | unsafe | assert | IF_COMPILE_TIME | ELSE_COMPILE_TIME | BUILTIN_GLOBAL
  private static boolean StatementRecover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StatementRecover_0")) return false;
    boolean r;
    r = consumeToken(b, NOT);
    if (!r) r = consumeToken(b, QUESTION);
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
    if (!r) r = consumeToken(b, FN);
    if (!r) r = consumeToken(b, PUB);
    if (!r) r = consumeToken(b, MUT);
    if (!r) r = consumeToken(b, GO);
    if (!r) r = consumeToken(b, GOTO);
    if (!r) r = consumeToken(b, HEX);
    if (!r) r = consumeToken(b, IDENTIFIER);
    if (!r) r = consumeToken(b, IF);
    if (!r) r = consumeToken(b, INT);
    if (!r) r = consumeToken(b, INTERFACE);
    if (!r) r = consumeToken(b, OCT);
    if (!r) r = consumeToken(b, RETURN);
    if (!r) r = consumeToken(b, SELECT);
    if (!r) r = consumeToken(b, STRING);
    if (!r) r = consumeToken(b, RAW_STRING);
    if (!r) r = consumeToken(b, STRUCT);
    if (!r) r = consumeToken(b, SWITCH);
    if (!r) r = consumeToken(b, VAR);
    if (!r) r = consumeToken(b, UNSAFE);
    if (!r) r = consumeToken(b, ASSERT);
    if (!r) r = consumeToken(b, IF_COMPILE_TIME);
    if (!r) r = consumeToken(b, ELSE_COMPILE_TIME);
    if (!r) r = consumeToken(b, BUILTIN_GLOBAL);
    return r;
  }

  /* ********************************************************** */
  // Statement (semi | &'}')
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

  // semi | &'}'
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
  // ConstDeclaration
  // //  | TypeDeclaration
  // //  | VarDeclaration
  //   | FunctionDeclaration
  //   | MethodDeclaration
  //   | StructDeclaration
  //   | GlobalVariableDeclaration
  static boolean TopDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TopDeclaration")) return false;
    boolean r;
    r = ConstDeclaration(b, l + 1);
    if (!r) r = FunctionDeclaration(b, l + 1);
    if (!r) r = MethodDeclaration(b, l + 1);
    if (!r) r = StructDeclaration(b, l + 1);
    if (!r) r = GlobalVariableDeclaration(b, l + 1);
    return r;
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

  // identifier?
  private static boolean StructType_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StructType_1")) return false;
    consumeToken(b, IDENTIFIER);
    return true;
  }

  // Fields?
  private static boolean StructType_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StructType_3")) return false;
    Fields(b, l + 1);
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
  // (pub | BUILTIN_GLOBAL)?
  public static boolean SymbolVisibility(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SymbolVisibility")) return false;
    Marker m = enter_section_(b, l, _NONE_, SYMBOL_VISIBILITY, "<symbol visibility>");
    SymbolVisibility_0(b, l + 1);
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  // pub | BUILTIN_GLOBAL
  private static boolean SymbolVisibility_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SymbolVisibility_0")) return false;
    boolean r;
    r = consumeToken(b, PUB);
    if (!r) r = consumeToken(b, BUILTIN_GLOBAL);
    return r;
  }

  /* ********************************************************** */
  // StringLiteral
  public static boolean Tag(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Tag")) return false;
    if (!nextTokenIs(b, "<tag>", RAW_STRING, STRING)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TAG, "<tag>");
    r = StringLiteral(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ArrayOrSliceType
  //   | StructType
  //   | PointerType
  //   | NullableType
  static boolean TypeLit(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeLit")) return false;
    boolean r;
    r = ArrayOrSliceType(b, l + 1);
    if (!r) r = StructType(b, l + 1);
    if (!r) r = PointerType(b, l + 1);
    if (!r) r = NullableType(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // TypeReferenceExpression (QualifiedTypeReferenceExpression)*
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

  // (QualifiedTypeReferenceExpression)*
  private static boolean TypeName_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeName_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!TypeName_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "TypeName_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // !(';' | type | const | fn | pub | BUILTIN_GLOBAL | var | struct | '[')
  static boolean TopLevelDeclarationRecover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TopLevelDeclarationRecover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !TopLevelDeclarationRecover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ';' | type | const | fn | pub | BUILTIN_GLOBAL | var | struct | '['
  private static boolean TopLevelDeclarationRecover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TopLevelDeclarationRecover_0")) return false;
    boolean r;
    r = consumeToken(b, SEMICOLON);
    if (!r) r = consumeToken(b, TYPE);
    if (!r) r = consumeToken(b, CONST);
    if (!r) r = consumeToken(b, FN);
    if (!r) r = consumeToken(b, PUB);
    if (!r) r = consumeToken(b, BUILTIN_GLOBAL);
    if (!r) r = consumeToken(b, VAR);
    if (!r) r = consumeToken(b, STRUCT);
    if (!r) r = consumeToken(b, LBRACK);
    return r;
  }

  /* ********************************************************** */
  // TypeName
  //   | TypeLit
  //   | ParType
  public static boolean Type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, TYPE, "<type>");
    r = TypeName(b, l + 1);
    if (!r) r = TypeLit(b, l + 1);
    if (!r) r = ParType(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // Type ( ',' Type )* ','?
  public static boolean TypeList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeList")) return false;
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

  /* ********************************************************** */
  // Expression root: Expression
  // Operator priority table:
  // 0: BINARY(OrExpr)
  // 1: BINARY(AndExpr)
  // 2: BINARY(ConditionalExpr)
  // 3: BINARY(AddExpr)
  // 4: BINARY(MulExpr)
  // 5: PREFIX(UnaryExpr)
  // 6: BINARY(RangeExpr)
  // 7: ATOM(StructInitialization)
  // 8: ATOM(UnsafeExpression)
  // 9: ATOM(IfExpression)
  // 10: ATOM(CompileTimeIfExpression)
  // 11: ATOM(ArrayCreation)
  // 12: ATOM(OperandName) POSTFIX(CallExpr) POSTFIX(IndexOrSliceExpr) ATOM(Literal)
  // 13: ATOM(ParenthesesExpr)
  public static boolean Expression(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "Expression")) return false;
    addVariant(b, "<expression>");
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<expression>");
    r = UnaryExpr(b, l + 1);
    if (!r) r = StructInitialization(b, l + 1);
    if (!r) r = UnsafeExpression(b, l + 1);
    if (!r) r = IfExpression(b, l + 1);
    if (!r) r = CompileTimeIfExpression(b, l + 1);
    if (!r) r = ArrayCreation(b, l + 1);
    if (!r) r = OperandName(b, l + 1);
    if (!r) r = Literal(b, l + 1);
    if (!r) r = ParenthesesExpr(b, l + 1);
    p = r;
    r = r && Expression_0(b, l + 1, g);
    exit_section_(b, l, m, null, r, p, null);
    return r || p;
  }

  // (QualifiedTypeReferenceExpression)
  private static boolean TypeName_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeName_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = QualifiedTypeReferenceExpression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
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
      else if (g < 6 && consumeTokenSmart(b, RANGE)) {
        r = Expression(b, l, 6);
        exit_section_(b, l, m, RANGE_EXPR, r, true, null);
      }
      else if (g < 12 && ArgumentList(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, CALL_EXPR, r, true, null);
      }
      else if (g < 12 && IndexOrSliceExpr_0(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, INDEX_OR_SLICE_EXPR, r, true, null);
      }
      else {
        exit_section_(b, l, m, null, false, false, null);
        break;
      }
    }
    return r;
  }

  // Type '{' ( '}' | FieldInitialization '}')
  public static boolean StructInitialization(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StructInitialization")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STRUCT_INITIALIZATION, "<struct initialization>");
    r = Type(b, l + 1);
    r = r && consumeToken(b, LBRACE);
    r = r && StructInitialization_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // UnsafeExpression
  public static boolean UnsafeStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "UnsafeStatement")) return false;
    if (!nextTokenIs(b, UNSAFE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = UnsafeExpression(b, l + 1);
    exit_section_(b, m, UNSAFE_STATEMENT, r);
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

  // '}' | FieldInitialization '}'
  private static boolean StructInitialization_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StructInitialization_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, RBRACE);
    if (!r) r = StructInitialization_2_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // FieldInitialization '}'
  private static boolean StructInitialization_2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StructInitialization_2_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = FieldInitialization(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, m, null, r);
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

  // unsafe Block
  public static boolean UnsafeExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "UnsafeExpression")) return false;
    if (!nextTokenIsSmart(b, UNSAFE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, UNSAFE_EXPRESSION, null);
    r = consumeTokenSmart(b, UNSAFE);
    p = r; // pin = 1
    r = r && Block(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ElseStatement?
  private static boolean IfExpression_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IfExpression_3")) return false;
    ElseStatement(b, l + 1);
    return true;
  }

  // IF_COMPILE_TIME Condition Block CompileElseStatement?
  public static boolean CompileTimeIfExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CompileTimeIfExpression")) return false;
    if (!nextTokenIsSmart(b, IF_COMPILE_TIME)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, COMPILE_TIME_IF_EXPRESSION, null);
    r = consumeTokenSmart(b, IF_COMPILE_TIME);
    p = r; // pin = 1
    r = r && report_error_(b, Condition(b, l + 1));
    r = p && report_error_(b, Block(b, l + 1)) && r;
    r = p && CompileTimeIfExpression_3(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // CompileElseStatement?
  private static boolean CompileTimeIfExpression_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CompileTimeIfExpression_3")) return false;
    CompileElseStatement(b, l + 1);
    return true;
  }

  // if Condition Block ElseStatement?
  public static boolean IfExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IfExpression")) return false;
    if (!nextTokenIsSmart(b, IF)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, IF_EXPRESSION, null);
    r = consumeTokenSmart(b, IF);
    p = r; // pin = 1
    r = r && report_error_(b, Condition(b, l + 1));
    r = p && report_error_(b, Block(b, l + 1)) && r;
    r = p && IfExpression_3(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // '[' ArrayCreationList ']'
  public static boolean ArrayCreation(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArrayCreation")) return false;
    if (!nextTokenIsSmart(b, LBRACK)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ARRAY_CREATION, null);
    r = consumeTokenSmart(b, LBRACK);
    p = r; // pin = 1
    r = r && report_error_(b, ArrayCreationList(b, l + 1));
    r = p && consumeToken(b, RBRACK) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
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

  // ReferenceExpression (QualifiedReferenceExpression)*
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

  // (QualifiedReferenceExpression)*
  private static boolean OperandName_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "OperandName_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!OperandName_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "OperandName_1", c)) break;
    }
    return true;
  }

  // (QualifiedReferenceExpression)
  private static boolean OperandName_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "OperandName_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = QualifiedReferenceExpression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '[' (SliceExprBody | IndexExprBody) ']'
  private static boolean IndexOrSliceExpr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IndexOrSliceExpr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, LBRACK);
    r = r && IndexOrSliceExpr_0_1(b, l + 1);
    r = r && consumeToken(b, RBRACK);
    exit_section_(b, m, null, r);
    return r;
  }

  // SliceExprBody | IndexExprBody
  private static boolean IndexOrSliceExpr_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IndexOrSliceExpr_0_1")) return false;
    boolean r;
    r = SliceExprBody(b, l + 1);
    if (!r) r = IndexExprBody(b, l + 1);
    return r;
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
