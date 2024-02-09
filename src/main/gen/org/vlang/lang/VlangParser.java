// This is a generated file. Not intended for manual editing.
package org.vlang.lang;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static org.vlang.lang.VlangTypes.*;
import static org.vlang.lang.VlangParserUtil.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class VlangParser implements PsiParser, LightPsiParser {

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

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return File(b, l + 1);
  }

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[] {
    create_token_set_(ARGUMENT_LIST, JSON_ARGUMENT_LIST),
    create_token_set_(GUARD_VAR_DECLARATION, RANGE_CLAUSE, VAR_DECLARATION),
    create_token_set_(SQL_BLOCK_STATEMENT, SQL_CREATE_STATEMENT, SQL_DELETE_STATEMENT, SQL_DROP_STATEMENT,
      SQL_INSERT_STATEMENT, SQL_SELECT_STATEMENT, SQL_UPDATE_STATEMENT),
    create_token_set_(APPEND_STATEMENT, ASM_BLOCK_STATEMENT, ASSERT_STATEMENT, ASSIGNMENT_STATEMENT,
      BREAK_STATEMENT, COMPILE_TIME_FOR_STATEMENT, CONTINUE_STATEMENT, DEFER_STATEMENT,
      FOR_STATEMENT, GOTO_STATEMENT, LABELED_STATEMENT, RETURN_STATEMENT,
      SELECT_ARM_ASSIGNMENT_STATEMENT, SELECT_ARM_STATEMENT, SEND_STATEMENT, SIMPLE_STATEMENT,
      STATEMENT),
    create_token_set_(ALIAS_TYPE, ANONYMOUS_STRUCT_TYPE, ARRAY_TYPE, ATOMIC_TYPE,
      CHANNEL_TYPE, ENUM_TYPE, FIXED_SIZE_ARRAY_TYPE, FUNCTION_TYPE,
      INTERFACE_TYPE, MAP_TYPE, NONE_TYPE, OPTION_TYPE,
      POINTER_TYPE, RESULT_TYPE, SHARED_TYPE, STRUCT_TYPE,
      THREAD_TYPE, TUPLE_TYPE, TYPE, WRONG_POINTER_TYPE),
    create_token_set_(ADD_EXPR, AND_EXPR, ARRAY_CREATION, AS_EXPRESSION,
      CALL_EXPR, CALL_EXPR_WITH_PROPAGATE, COMPILE_TIME_IF_EXPRESSION, CONDITIONAL_EXPR,
      DOT_EXPRESSION, DUMP_CALL_EXPR, ENUM_FETCH, EXPRESSION,
      FUNCTION_LIT, GO_EXPRESSION, IF_EXPRESSION, INC_DEC_EXPRESSION,
      INDEX_OR_SLICE_EXPR, IN_EXPRESSION, IS_EXPRESSION, IS_REF_TYPE_CALL_EXPR,
      JSON_CALL_EXPR, LITERAL, LITERAL_VALUE_EXPRESSION, LOCK_EXPRESSION,
      MAP_INIT_EXPR, MATCH_EXPRESSION, MUL_EXPR, MUT_EXPRESSION,
      NOT_IN_EXPRESSION, NOT_IS_EXPRESSION, OFFSET_OF_CALL_EXPR, OR_BLOCK_EXPR,
      OR_EXPR, PARENTHESES_EXPR, RANGE_EXPR, REFERENCE_EXPRESSION,
      SELECT_EXPRESSION, SEND_EXPR, SHARED_EXPRESSION, SHIFT_LEFT_EXPR,
      SIZE_OF_CALL_EXPR, SPAWN_EXPRESSION, SQL_EXPRESSION, STRING_LITERAL,
      TYPE_CAST_EXPRESSION, TYPE_OF_CALL_EXPR, UNARY_EXPR, UNPACKING_EXPRESSION,
      UNSAFE_EXPRESSION),
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
  // identifier GenericParameters? '=' TypeUnionList
  public static boolean AliasType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AliasType")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ALIAS_TYPE, null);
    r = consumeToken(b, IDENTIFIER);
    r = r && AliasType_1(b, l + 1);
    p = r; // pin = 2
    r = r && report_error_(b, consumeToken(b, ASSIGN));
    r = p && TypeUnionList(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // GenericParameters?
  private static boolean AliasType_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AliasType_1")) return false;
    GenericParameters(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // struct '{' FieldsGroup* '}'
  public static boolean AnonymousStructType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AnonymousStructType")) return false;
    if (!nextTokenIs(b, STRUCT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ANONYMOUS_STRUCT_TYPE, null);
    r = consumeTokens(b, 2, STRUCT, LBRACE);
    p = r; // pin = 2
    r = r && report_error_(b, AnonymousStructType_2(b, l + 1));
    r = p && consumeToken(b, RBRACE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // FieldsGroup*
  private static boolean AnonymousStructType_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AnonymousStructType_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!FieldsGroup(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "AnonymousStructType_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // struct '{' ElementList? '}'
  public static boolean AnonymousStructValueExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AnonymousStructValueExpression")) return false;
    if (!nextTokenIs(b, STRUCT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ANONYMOUS_STRUCT_VALUE_EXPRESSION, null);
    r = consumeTokens(b, 2, STRUCT, LBRACE);
    p = r; // pin = 2
    r = r && report_error_(b, AnonymousStructValueExpression_2(b, l + 1));
    r = p && consumeToken(b, RBRACE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ElementList?
  private static boolean AnonymousStructValueExpression_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AnonymousStructValueExpression_2")) return false;
    ElementList(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // DotExpression ShiftLeftOp Expression
  public static boolean AppendStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AppendStatement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, APPEND_STATEMENT, "<append statement>");
    r = DotExpression(b, l + 1);
    r = r && ShiftLeftOp(b, l + 1);
    r = r && Expression(b, l + 1, -1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '(' <<enterMode "PAR">> ElementList? '...'? ','? <<exitModeSafe "PAR">> ')'
  public static boolean ArgumentList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArgumentList")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ARGUMENT_LIST, null);
    r = consumeToken(b, LPAREN);
    p = r; // pin = 1
    r = r && report_error_(b, enterMode(b, l + 1, "PAR"));
    r = p && report_error_(b, ArgumentList_2(b, l + 1)) && r;
    r = p && report_error_(b, ArgumentList_3(b, l + 1)) && r;
    r = p && report_error_(b, ArgumentList_4(b, l + 1)) && r;
    r = p && report_error_(b, exitModeSafe(b, l + 1, "PAR")) && r;
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ElementList?
  private static boolean ArgumentList_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArgumentList_2")) return false;
    ElementList(b, l + 1);
    return true;
  }

  // '...'?
  private static boolean ArgumentList_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArgumentList_3")) return false;
    consumeToken(b, TRIPLE_DOT);
    return true;
  }

  // ','?
  private static boolean ArgumentList_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArgumentList_4")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // '[' <<enterMode "PAR">> ArrayCreationList? semi? (<<exitModeSafe "PAR">> ']' '!'?) !identifier
  public static boolean ArrayCreation(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArrayCreation")) return false;
    if (!nextTokenIs(b, LBRACK)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACK);
    r = r && enterMode(b, l + 1, "PAR");
    r = r && ArrayCreation_2(b, l + 1);
    r = r && ArrayCreation_3(b, l + 1);
    r = r && ArrayCreation_4(b, l + 1);
    r = r && ArrayCreation_5(b, l + 1);
    exit_section_(b, m, ARRAY_CREATION, r);
    return r;
  }

  // ArrayCreationList?
  private static boolean ArrayCreation_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArrayCreation_2")) return false;
    ArrayCreationList(b, l + 1);
    return true;
  }

  // semi?
  private static boolean ArrayCreation_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArrayCreation_3")) return false;
    semi(b, l + 1);
    return true;
  }

  // <<exitModeSafe "PAR">> ']' '!'?
  private static boolean ArrayCreation_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArrayCreation_4")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = exitModeSafe(b, l + 1, "PAR");
    r = r && consumeToken(b, RBRACK);
    r = r && ArrayCreation_4_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '!'?
  private static boolean ArrayCreation_4_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArrayCreation_4_2")) return false;
    consumeToken(b, NOT);
    return true;
  }

  // !identifier
  private static boolean ArrayCreation_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArrayCreation_5")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, IDENTIFIER);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ExpressionWithRecover (list_separator? ExpressionWithRecover)* list_separator?
  public static boolean ArrayCreationList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArrayCreationList")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ARRAY_CREATION_LIST, "<array creation list>");
    r = ExpressionWithRecover(b, l + 1);
    r = r && ArrayCreationList_1(b, l + 1);
    r = r && ArrayCreationList_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (list_separator? ExpressionWithRecover)*
  private static boolean ArrayCreationList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArrayCreationList_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ArrayCreationList_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ArrayCreationList_1", c)) break;
    }
    return true;
  }

  // list_separator? ExpressionWithRecover
  private static boolean ArrayCreationList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArrayCreationList_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ArrayCreationList_1_0_0(b, l + 1);
    r = r && ExpressionWithRecover(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // list_separator?
  private static boolean ArrayCreationList_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArrayCreationList_1_0_0")) return false;
    list_separator(b, l + 1);
    return true;
  }

  // list_separator?
  private static boolean ArrayCreationList_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArrayCreationList_2")) return false;
    list_separator(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // '['']' Type
  public static boolean ArrayType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArrayType")) return false;
    if (!nextTokenIs(b, LBRACK)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, LBRACK, RBRACK);
    r = r && Type(b, l + 1);
    exit_section_(b, m, ARRAY_TYPE, r);
    return r;
  }

  /* ********************************************************** */
  // '{' ('}' | (ASM_LINE)* '}')
  public static boolean AsmBlock(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmBlock")) return false;
    if (!nextTokenIs(b, LBRACE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ASM_BLOCK, null);
    r = consumeToken(b, LBRACE);
    p = r; // pin = 1
    r = r && AsmBlock_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // '}' | (ASM_LINE)* '}'
  private static boolean AsmBlock_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmBlock_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, RBRACE);
    if (!r) r = AsmBlock_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (ASM_LINE)* '}'
  private static boolean AsmBlock_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmBlock_1_1")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = AsmBlock_1_1_0(b, l + 1);
    p = r; // pin = 1
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (ASM_LINE)*
  private static boolean AsmBlock_1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmBlock_1_1_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, ASM_LINE)) break;
      if (!empty_element_parsed_guard_(b, "AsmBlock_1_1_0", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // asm volatile? identifier AsmBlock
  public static boolean AsmBlockStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmBlockStatement")) return false;
    if (!nextTokenIs(b, ASM)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ASM_BLOCK_STATEMENT, null);
    r = consumeToken(b, ASM);
    p = r; // pin = 1
    r = r && report_error_(b, AsmBlockStatement_1(b, l + 1));
    r = p && report_error_(b, consumeToken(b, IDENTIFIER)) && r;
    r = p && AsmBlock(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // volatile?
  private static boolean AsmBlockStatement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsmBlockStatement_1")) return false;
    consumeToken(b, VOLATILE);
    return true;
  }

  /* ********************************************************** */
  // assert Expression (',' Expression)?
  public static boolean AssertStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AssertStatement")) return false;
    if (!nextTokenIs(b, ASSERT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ASSERT);
    r = r && Expression(b, l + 1, -1);
    r = r && AssertStatement_2(b, l + 1);
    exit_section_(b, m, ASSERT_STATEMENT, r);
    return r;
  }

  // (',' Expression)?
  private static boolean AssertStatement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AssertStatement_2")) return false;
    AssertStatement_2_0(b, l + 1);
    return true;
  }

  // ',' Expression
  private static boolean AssertStatement_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AssertStatement_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && Expression(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '=' | '+=' | '-=' | '|=' | '^=' | '*=' | '/=' | '%=' | '<<=' | '>>=' | '>>>=' | '&=' | '&^='
  public static boolean AssignOp(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AssignOp")) return false;
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
    if (!r) r = consumeToken(b, UNSIGNED_SHIFT_RIGHT_ASSIGN);
    if (!r) r = consumeToken(b, BIT_AND_ASSIGN);
    if (!r) r = consumeToken(b, BIT_CLEAR_ASSIGN);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // AssignOp (ExpressionList)
  public static boolean AssignmentStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AssignmentStatement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _LEFT_, ASSIGNMENT_STATEMENT, "<assignment statement>");
    r = AssignOp(b, l + 1);
    p = r; // pin = 1
    r = r && AssignmentStatement_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (ExpressionList)
  private static boolean AssignmentStatement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AssignmentStatement_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ExpressionList(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // atomic Type?
  public static boolean AtomicType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AtomicType")) return false;
    if (!nextTokenIs(b, ATOMIC)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ATOMIC_TYPE, null);
    r = consumeToken(b, ATOMIC);
    p = r; // pin = 1
    r = r && AtomicType_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Type?
  private static boolean AtomicType_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AtomicType_1")) return false;
    Type(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // '[' AttributeExpressions ']'
  public static boolean Attribute(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Attribute")) return false;
    if (!nextTokenIs(b, LBRACK)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ATTRIBUTE, null);
    r = consumeToken(b, LBRACK);
    p = r; // pin = 1
    r = r && report_error_(b, AttributeExpressions(b, l + 1));
    r = p && consumeToken(b, RBRACK) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
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
  // AttributeExpression (';' AttributeExpression)*
  static boolean AttributeExpressions(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AttributeExpressions")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = AttributeExpression(b, l + 1);
    p = r; // pin = 1
    r = r && AttributeExpressions_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (';' AttributeExpression)*
  private static boolean AttributeExpressions_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AttributeExpressions_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!AttributeExpressions_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "AttributeExpressions_1", c)) break;
    }
    return true;
  }

  // ';' AttributeExpression
  private static boolean AttributeExpressions_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AttributeExpressions_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, SEMICOLON);
    p = r; // pin = 1
    r = r && AttributeExpression(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // AttributeIdentifierPrefix? (identifier | unsafe)
  public static boolean AttributeIdentifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AttributeIdentifier")) return false;
    if (!nextTokenIs(b, "<attribute identifier>", IDENTIFIER, UNSAFE)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ATTRIBUTE_IDENTIFIER, "<attribute identifier>");
    r = AttributeIdentifier_0(b, l + 1);
    r = r && AttributeIdentifier_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // AttributeIdentifierPrefix?
  private static boolean AttributeIdentifier_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AttributeIdentifier_0")) return false;
    AttributeIdentifierPrefix(b, l + 1);
    return true;
  }

  // identifier | unsafe
  private static boolean AttributeIdentifier_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AttributeIdentifier_1")) return false;
    boolean r;
    r = consumeToken(b, IDENTIFIER);
    if (!r) r = consumeToken(b, UNSAFE);
    return r;
  }

  /* ********************************************************** */
  // (identifier | unsafe) '.'
  public static boolean AttributeIdentifierPrefix(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AttributeIdentifierPrefix")) return false;
    if (!nextTokenIs(b, "<attribute identifier prefix>", IDENTIFIER, UNSAFE)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ATTRIBUTE_IDENTIFIER_PREFIX, "<attribute identifier prefix>");
    r = AttributeIdentifierPrefix_0(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // identifier | unsafe
  private static boolean AttributeIdentifierPrefix_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AttributeIdentifierPrefix_0")) return false;
    boolean r;
    r = consumeToken(b, IDENTIFIER);
    if (!r) r = consumeToken(b, UNSAFE);
    return r;
  }

  /* ********************************************************** */
  // AttributeIdentifier | Literal
  public static boolean AttributeKey(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AttributeKey")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ATTRIBUTE_KEY, "<attribute key>");
    r = AttributeIdentifier(b, l + 1);
    if (!r) r = Literal(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // identifier | Literal
  public static boolean AttributeValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AttributeValue")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ATTRIBUTE_VALUE, "<attribute value>");
    r = consumeToken(b, IDENTIFIER);
    if (!r) r = Literal(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ':' AttributeValue
  static boolean AttributeValueWithColon(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AttributeValueWithColon")) return false;
    if (!nextTokenIs(b, COLON)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COLON);
    p = r; // pin = 1
    r = r && AttributeValue(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
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

  // semi?
  private static boolean Attributes_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Attributes_2")) return false;
    semi(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // BeforeBlockExpressionInner | !() Expression
  static boolean BeforeBlockExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BeforeBlockExpression")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = beforeBlockExpression(b, l + 1);
    if (!r) r = BeforeBlockExpression_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !() Expression
  private static boolean BeforeBlockExpression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BeforeBlockExpression_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = BeforeBlockExpression_1_0(b, l + 1);
    r = r && Expression(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !()
  private static boolean BeforeBlockExpression_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BeforeBlockExpression_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !BeforeBlockExpression_1_0_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ()
  private static boolean BeforeBlockExpression_1_0_0(PsiBuilder b, int l) {
    return true;
  }

  /* ********************************************************** */
  // BeforeBlockExpression (',' (BeforeBlockExpression | &')'))*
  static boolean BeforeBlockExpressionList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BeforeBlockExpressionList")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = BeforeBlockExpression(b, l + 1);
    p = r; // pin = 1
    r = r && BeforeBlockExpressionList_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (',' (BeforeBlockExpression | &')'))*
  private static boolean BeforeBlockExpressionList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BeforeBlockExpressionList_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!BeforeBlockExpressionList_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "BeforeBlockExpressionList_1", c)) break;
    }
    return true;
  }

  // ',' (BeforeBlockExpression | &')')
  private static boolean BeforeBlockExpressionList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BeforeBlockExpressionList_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && BeforeBlockExpressionList_1_0_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // BeforeBlockExpression | &')'
  private static boolean BeforeBlockExpressionList_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BeforeBlockExpressionList_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = BeforeBlockExpression(b, l + 1);
    if (!r) r = BeforeBlockExpressionList_1_0_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // &')'
  private static boolean BeforeBlockExpressionList_1_0_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BeforeBlockExpressionList_1_0_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = consumeToken(b, RPAREN);
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
  // '{' ('}' | (<<withOff Statements "BLOCK?" "PAR">> | (!() Statements)) '}')
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

  // '}' | (<<withOff Statements "BLOCK?" "PAR">> | (!() Statements)) '}'
  private static boolean BlockInner_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BlockInner_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, RBRACE);
    if (!r) r = BlockInner_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (<<withOff Statements "BLOCK?" "PAR">> | (!() Statements)) '}'
  private static boolean BlockInner_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BlockInner_1_1")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = BlockInner_1_1_0(b, l + 1);
    p = r; // pin = 1
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // <<withOff Statements "BLOCK?" "PAR">> | (!() Statements)
  private static boolean BlockInner_1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BlockInner_1_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = withOff(b, l + 1, VlangParser::Statements, "BLOCK?", "PAR");
    if (!r) r = BlockInner_1_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !() Statements
  private static boolean BlockInner_1_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BlockInner_1_1_0_1")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = BlockInner_1_1_0_1_0(b, l + 1);
    p = r; // pin = 1
    r = r && Statements(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // !()
  private static boolean BlockInner_1_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BlockInner_1_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !BlockInner_1_1_0_1_0_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ()
  private static boolean BlockInner_1_1_0_1_0_0(PsiBuilder b, int l) {
    return true;
  }

  /* ********************************************************** */
  // '{' ('}' | (<<withOff Statements "BLOCK?" "PAR">> | (!() Statements)) '}')
  public static boolean BlockNoPin(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BlockNoPin")) return false;
    if (!nextTokenIs(b, LBRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACE);
    r = r && BlockNoPin_1(b, l + 1);
    exit_section_(b, m, BLOCK, r);
    return r;
  }

  // '}' | (<<withOff Statements "BLOCK?" "PAR">> | (!() Statements)) '}'
  private static boolean BlockNoPin_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BlockNoPin_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, RBRACE);
    if (!r) r = BlockNoPin_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (<<withOff Statements "BLOCK?" "PAR">> | (!() Statements)) '}'
  private static boolean BlockNoPin_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BlockNoPin_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = BlockNoPin_1_1_0(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  // <<withOff Statements "BLOCK?" "PAR">> | (!() Statements)
  private static boolean BlockNoPin_1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BlockNoPin_1_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = withOff(b, l + 1, VlangParser::Statements, "BLOCK?", "PAR");
    if (!r) r = BlockNoPin_1_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !() Statements
  private static boolean BlockNoPin_1_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BlockNoPin_1_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = BlockNoPin_1_1_0_1_0(b, l + 1);
    r = r && Statements(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !()
  private static boolean BlockNoPin_1_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BlockNoPin_1_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !BlockNoPin_1_1_0_1_0_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ()
  private static boolean BlockNoPin_1_1_0_1_0_0(PsiBuilder b, int l) {
    return true;
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

  /* ********************************************************** */
  // OffsetOfCallExpr | TypeOfCallExpr | SizeOfCallExpr | IsRefTypeCallExpr | DumpCallExpr
  static boolean BuiltinCall(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BuiltinCall")) return false;
    boolean r;
    r = OffsetOfCallExpr(b, l + 1);
    if (!r) r = TypeOfCallExpr(b, l + 1);
    if (!r) r = SizeOfCallExpr(b, l + 1);
    if (!r) r = IsRefTypeCallExpr(b, l + 1);
    if (!r) r = DumpCallExpr(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // GenericArguments? ArgumentList
  public static boolean CallExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CallExpr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_, CALL_EXPR, "<call expr>");
    r = CallExpr_0(b, l + 1);
    r = r && ArgumentList(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // GenericArguments?
  private static boolean CallExpr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CallExpr_0")) return false;
    GenericArguments(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // GenericArguments? ArgumentList ('!' | '?')
  public static boolean CallExprWithPropagate(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CallExprWithPropagate")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_, CALL_EXPR_WITH_PROPAGATE, "<call expr with propagate>");
    r = CallExprWithPropagate_0(b, l + 1);
    r = r && ArgumentList(b, l + 1);
    r = r && CallExprWithPropagate_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // GenericArguments?
  private static boolean CallExprWithPropagate_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CallExprWithPropagate_0")) return false;
    GenericArguments(b, l + 1);
    return true;
  }

  // '!' | '?'
  private static boolean CallExprWithPropagate_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CallExprWithPropagate_2")) return false;
    boolean r;
    r = consumeToken(b, NOT);
    if (!r) r = consumeToken(b, QUESTION);
    return r;
  }

  /* ********************************************************** */
  // VarModifiers? ReferenceExpression
  public static boolean Capture(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Capture")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CAPTURE, "<capture>");
    r = Capture_0(b, l + 1);
    r = r && ReferenceExpression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // VarModifiers?
  private static boolean Capture_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Capture_0")) return false;
    VarModifiers(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // '[' (Capture | ',' Capture)* ']'
  public static boolean CaptureList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CaptureList")) return false;
    if (!nextTokenIs(b, LBRACK)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CAPTURE_LIST, null);
    r = consumeToken(b, LBRACK);
    p = r; // pin = 1
    r = r && report_error_(b, CaptureList_1(b, l + 1));
    r = p && consumeToken(b, RBRACK) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (Capture | ',' Capture)*
  private static boolean CaptureList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CaptureList_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!CaptureList_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "CaptureList_1", c)) break;
    }
    return true;
  }

  // Capture | ',' Capture
  private static boolean CaptureList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CaptureList_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = Capture(b, l + 1);
    if (!r) r = CaptureList_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ',' Capture
  private static boolean CaptureList_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CaptureList_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && Capture(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'chan' Type?
  public static boolean ChannelType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ChannelType")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CHANNEL_TYPE, "<channel type>");
    r = consumeToken(b, "chan");
    p = r; // pin = 1
    r = r && ChannelType_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Type?
  private static boolean ChannelType_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ChannelType_1")) return false;
    Type(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // '$else' (CompileTimeIfExpression | Block)
  public static boolean CompileTimeElseBranch(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CompileTimeElseBranch")) return false;
    if (!nextTokenIs(b, ELSE_COMPILE_TIME)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, COMPILE_TIME_ELSE_BRANCH, null);
    r = consumeToken(b, ELSE_COMPILE_TIME);
    p = r; // pin = 1
    r = r && CompileTimeElseBranch_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // CompileTimeIfExpression | Block
  private static boolean CompileTimeElseBranch_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CompileTimeElseBranch_1")) return false;
    boolean r;
    r = CompileTimeIfExpression(b, l + 1);
    if (!r) r = Block(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // '.' '$' '(' DotExpression ')'
  public static boolean CompileTimeFieldReference(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CompileTimeFieldReference")) return false;
    if (!nextTokenIs(b, DOT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _LEFT_, COMPILE_TIME_FIELD_REFERENCE, null);
    r = consumeTokens(b, 2, DOT, DOLLAR, LPAREN);
    p = r; // pin = 2
    r = r && report_error_(b, DotExpression(b, l + 1));
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // '$for' <<enterMode "BLOCK?">> (ForClause | RangeClause | Expression) Block <<exitModeSafe "BLOCK?">>
  public static boolean CompileTimeForStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CompileTimeForStatement")) return false;
    if (!nextTokenIs(b, FOR_COMPILE_TIME)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, COMPILE_TIME_FOR_STATEMENT, null);
    r = consumeToken(b, FOR_COMPILE_TIME);
    p = r; // pin = 1
    r = r && report_error_(b, enterMode(b, l + 1, "BLOCK?"));
    r = p && report_error_(b, CompileTimeForStatement_2(b, l + 1)) && r;
    r = p && report_error_(b, Block(b, l + 1)) && r;
    r = p && exitModeSafe(b, l + 1, "BLOCK?") && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ForClause | RangeClause | Expression
  private static boolean CompileTimeForStatement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CompileTimeForStatement_2")) return false;
    boolean r;
    r = ForClause(b, l + 1);
    if (!r) r = RangeClause(b, l + 1);
    if (!r) r = Expression(b, l + 1, -1);
    return r;
  }

  /* ********************************************************** */
  // GuardVarDeclaration | BeforeBlockExpression
  static boolean Condition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Condition")) return false;
    boolean r;
    r = GuardVarDeclaration(b, l + 1);
    if (!r) r = BeforeBlockExpression(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // Attributes? SymbolVisibility? const ( ConstDefinition | '(' ConstDefinitions? ')' )
  public static boolean ConstDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstDeclaration")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CONST_DECLARATION, "<const declaration>");
    r = ConstDeclaration_0(b, l + 1);
    r = r && ConstDeclaration_1(b, l + 1);
    r = r && consumeToken(b, CONST);
    p = r; // pin = 3
    r = r && ConstDeclaration_3(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Attributes?
  private static boolean ConstDeclaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstDeclaration_0")) return false;
    Attributes(b, l + 1);
    return true;
  }

  // SymbolVisibility?
  private static boolean ConstDeclaration_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstDeclaration_1")) return false;
    SymbolVisibility(b, l + 1);
    return true;
  }

  // ConstDefinition | '(' ConstDefinitions? ')'
  private static boolean ConstDeclaration_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstDeclaration_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ConstDefinition(b, l + 1);
    if (!r) r = ConstDeclaration_3_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '(' ConstDefinitions? ')'
  private static boolean ConstDeclaration_3_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstDeclaration_3_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && ConstDeclaration_3_1_1(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // ConstDefinitions?
  private static boolean ConstDeclaration_3_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstDeclaration_3_1_1")) return false;
    ConstDefinitions(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // identifier '=' (<<withOff Expression "BLOCK?">> | (!() Expression))
  public static boolean ConstDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstDefinition")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CONST_DEFINITION, null);
    r = consumeTokens(b, 2, IDENTIFIER, ASSIGN);
    p = r; // pin = 2
    r = r && ConstDefinition_2(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // <<withOff Expression "BLOCK?">> | (!() Expression)
  private static boolean ConstDefinition_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstDefinition_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = withOff(b, l + 1, Expression_parser_, "BLOCK?");
    if (!r) r = ConstDefinition_2_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !() Expression
  private static boolean ConstDefinition_2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstDefinition_2_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ConstDefinition_2_1_0(b, l + 1);
    r = r && Expression(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !()
  private static boolean ConstDefinition_2_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstDefinition_2_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !ConstDefinition_2_1_0_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ()
  private static boolean ConstDefinition_2_1_0_0(PsiBuilder b, int l) {
    return true;
  }

  /* ********************************************************** */
  // ConstDefinition (semi ConstDefinition)* semi?
  static boolean ConstDefinitions(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstDefinitions")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = ConstDefinition(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, ConstDefinitions_1(b, l + 1));
    r = p && ConstDefinitions_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (semi ConstDefinition)*
  private static boolean ConstDefinitions_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstDefinitions_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ConstDefinitions_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ConstDefinitions_1", c)) break;
    }
    return true;
  }

  // semi ConstDefinition
  private static boolean ConstDefinitions_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstDefinitions_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = semi(b, l + 1);
    r = r && ConstDefinition(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // semi?
  private static boolean ConstDefinitions_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstDefinitions_2")) return false;
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
  // '=' Expression
  public static boolean DefaultFieldValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "DefaultFieldValue")) return false;
    if (!nextTokenIs(b, ASSIGN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, DEFAULT_FIELD_VALUE, null);
    r = consumeToken(b, ASSIGN);
    p = r; // pin = 1
    r = r && Expression(b, l + 1, -1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
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

  /* ********************************************************** */
  // AnonymousStructValueExpression
  //   | SqlExpression
  //   | LiteralValueExpression
  //   | BuiltinCall
  //   | IfExpression
  //   | ReferenceExpression
  //   | Literal
  //   | FunctionLit
  //   | ArrayCreation
  //   | ParenthesesExpr
  //   | UnsafeExpression
  //   | MapInitExpr
  static boolean DotPrimaryExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "DotPrimaryExpr")) return false;
    boolean r;
    r = AnonymousStructValueExpression(b, l + 1);
    if (!r) r = SqlExpression(b, l + 1);
    if (!r) r = LiteralValueExpression(b, l + 1);
    if (!r) r = BuiltinCall(b, l + 1);
    if (!r) r = IfExpression(b, l + 1);
    if (!r) r = ReferenceExpression(b, l + 1);
    if (!r) r = Literal(b, l + 1);
    if (!r) r = FunctionLit(b, l + 1);
    if (!r) r = ArrayCreation(b, l + 1);
    if (!r) r = ParenthesesExpr(b, l + 1);
    if (!r) r = UnsafeExpression(b, l + 1);
    if (!r) r = MapInitExpr(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // dump '(' Expression ')'
  public static boolean DumpCallExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "DumpCallExpr")) return false;
    if (!nextTokenIs(b, DUMP)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, DUMP_CALL_EXPR, null);
    r = consumeTokens(b, 1, DUMP, LPAREN);
    p = r; // pin = 1
    r = r && report_error_(b, Expression(b, l + 1, -1));
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // First [':' Value]
  public static boolean Element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Element")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ELEMENT, "<element>");
    r = First(b, l + 1);
    p = r; // pin = 1
    r = r && Element_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [':' Value]
  private static boolean Element_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Element_1")) return false;
    Element_1_0(b, l + 1);
    return true;
  }

  // ':' Value
  private static boolean Element_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Element_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COLON);
    p = r; // pin = 1
    r = r && Value(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // Element | (!() Element)
  static boolean ElementInner(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ElementInner")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = Element(b, l + 1);
    if (!r) r = ElementInner_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !() Element
  private static boolean ElementInner_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ElementInner_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ElementInner_1_0(b, l + 1);
    r = r && Element(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !()
  private static boolean ElementInner_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ElementInner_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !ElementInner_1_0_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ()
  private static boolean ElementInner_1_0_0(PsiBuilder b, int l) {
    return true;
  }

  /* ********************************************************** */
  // ElementInner ((',' | semi) ElementInner?)*
  static boolean ElementList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ElementList")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = ElementInner(b, l + 1);
    p = r; // pin = 1
    r = r && ElementList_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ((',' | semi) ElementInner?)*
  private static boolean ElementList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ElementList_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ElementList_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ElementList_1", c)) break;
    }
    return true;
  }

  // (',' | semi) ElementInner?
  private static boolean ElementList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ElementList_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = ElementList_1_0_0(b, l + 1);
    p = r; // pin = 1
    r = r && ElementList_1_0_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ',' | semi
  private static boolean ElementList_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ElementList_1_0_0")) return false;
    boolean r;
    r = consumeToken(b, COMMA);
    if (!r) r = semi(b, l + 1);
    return r;
  }

  // ElementInner?
  private static boolean ElementList_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ElementList_1_0_1")) return false;
    ElementInner(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // else (IfExpression | Block)
  public static boolean ElseBranch(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ElseBranch")) return false;
    if (!nextTokenIs(b, ELSE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ELSE_BRANCH, null);
    r = consumeToken(b, ELSE);
    p = r; // pin = 1
    r = r && ElseBranch_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // IfExpression | Block
  private static boolean ElseBranch_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ElseBranch_1")) return false;
    boolean r;
    r = IfExpression(b, l + 1);
    if (!r) r = Block(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // Type
  public static boolean EmbeddedDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EmbeddedDefinition")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EMBEDDED_DEFINITION, "<embedded definition>");
    r = Type(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // Type
  public static boolean EmbeddedInterfaceDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EmbeddedInterfaceDefinition")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EMBEDDED_INTERFACE_DEFINITION, "<embedded interface definition>");
    r = Type(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '..' &']'
  public static boolean EmptySlice(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EmptySlice")) return false;
    if (!nextTokenIs(b, RANGE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, RANGE);
    r = r && EmptySlice_1(b, l + 1);
    exit_section_(b, m, EMPTY_SLICE, r);
    return r;
  }

  // &']'
  private static boolean EmptySlice_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EmptySlice_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = consumeToken(b, RBRACK);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // as Type
  public static boolean EnumBackedTypeAs(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumBackedTypeAs")) return false;
    if (!nextTokenIs(b, AS)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ENUM_BACKED_TYPE_AS, null);
    r = consumeToken(b, AS);
    p = r; // pin = 1
    r = r && Type(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // Attributes? SymbolVisibility? EnumType
  public static boolean EnumDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumDeclaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ENUM_DECLARATION, "<enum declaration>");
    r = EnumDeclaration_0(b, l + 1);
    r = r && EnumDeclaration_1(b, l + 1);
    r = r && EnumType(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // Attributes?
  private static boolean EnumDeclaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumDeclaration_0")) return false;
    Attributes(b, l + 1);
    return true;
  }

  // SymbolVisibility?
  private static boolean EnumDeclaration_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumDeclaration_1")) return false;
    SymbolVisibility(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // EnumFieldDefinition ('=' Expression)? semi?
  public static boolean EnumFieldDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumFieldDeclaration")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ENUM_FIELD_DECLARATION, null);
    r = EnumFieldDefinition(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, EnumFieldDeclaration_1(b, l + 1));
    r = p && EnumFieldDeclaration_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ('=' Expression)?
  private static boolean EnumFieldDeclaration_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumFieldDeclaration_1")) return false;
    EnumFieldDeclaration_1_0(b, l + 1);
    return true;
  }

  // '=' Expression
  private static boolean EnumFieldDeclaration_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumFieldDeclaration_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ASSIGN);
    r = r && Expression(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // semi?
  private static boolean EnumFieldDeclaration_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumFieldDeclaration_2")) return false;
    semi(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // identifier
  public static boolean EnumFieldDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumFieldDefinition")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, ENUM_FIELD_DEFINITION, r);
    return r;
  }

  /* ********************************************************** */
  // EnumFieldDeclaration (EnumFieldDeclaration)*
  static boolean EnumFields(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumFields")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = EnumFieldDeclaration(b, l + 1);
    r = r && EnumFields_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (EnumFieldDeclaration)*
  private static boolean EnumFields_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumFields_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!EnumFields_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "EnumFields_1", c)) break;
    }
    return true;
  }

  // (EnumFieldDeclaration)
  private static boolean EnumFields_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumFields_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = EnumFieldDeclaration(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // enum identifier EnumBackedTypeAs? '{' EnumFields? '}'
  public static boolean EnumType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumType")) return false;
    if (!nextTokenIs(b, ENUM)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ENUM_TYPE, null);
    r = consumeTokens(b, 1, ENUM, IDENTIFIER);
    p = r; // pin = 1
    r = r && report_error_(b, EnumType_2(b, l + 1));
    r = p && report_error_(b, consumeToken(b, LBRACE)) && r;
    r = p && report_error_(b, EnumType_4(b, l + 1)) && r;
    r = p && consumeToken(b, RBRACE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // EnumBackedTypeAs?
  private static boolean EnumType_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumType_2")) return false;
    EnumBackedTypeAs(b, l + 1);
    return true;
  }

  // EnumFields?
  private static boolean EnumType_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumType_4")) return false;
    EnumFields(b, l + 1);
    return true;
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
  // !('!' | '?' | '!=' | '%' | '%=' | '&&' | '&' | '&=' | '&^' | '&^=' | '(' | ')' | '*' | '*=' | '+'  | '++' | '+=' | ',' | '-' | '--' | '-=' | '...' | '/' | '/=' | ':' | ';' | '<' | '<-' | '<<' | '<<=' | '<=' | '<NL>' | '=' | '==' | '>' | '>=' | '>>' | '>>=' | '>>>=' | '[' | ']' | '^' | '^=' | '{' | '|' | '|=' | '||' | '}' | 'type' | break | case | const | continue | defer | else | float | for | fn | go | spawn | goto | hex | identifier | if | int | interface | oct | return | select | 'raw_string' | OPEN_QUOTE | char | struct | enum | union | var | unsafe | assert | match | asm | true | false | none | nil | typeof | offsetof | sizeof | isreftype | dump )
  static boolean ExpressionListRecover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ExpressionListRecover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !ExpressionListRecover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '!' | '?' | '!=' | '%' | '%=' | '&&' | '&' | '&=' | '&^' | '&^=' | '(' | ')' | '*' | '*=' | '+'  | '++' | '+=' | ',' | '-' | '--' | '-=' | '...' | '/' | '/=' | ':' | ';' | '<' | '<-' | '<<' | '<<=' | '<=' | '<NL>' | '=' | '==' | '>' | '>=' | '>>' | '>>=' | '>>>=' | '[' | ']' | '^' | '^=' | '{' | '|' | '|=' | '||' | '}' | 'type' | break | case | const | continue | defer | else | float | for | fn | go | spawn | goto | hex | identifier | if | int | interface | oct | return | select | 'raw_string' | OPEN_QUOTE | char | struct | enum | union | var | unsafe | assert | match | asm | true | false | none | nil | typeof | offsetof | sizeof | isreftype | dump
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
    if (!r) r = consumeToken(b, UNSIGNED_SHIFT_RIGHT_ASSIGN);
    if (!r) r = consumeToken(b, LBRACK);
    if (!r) r = consumeToken(b, RBRACK);
    if (!r) r = consumeToken(b, BIT_XOR);
    if (!r) r = consumeToken(b, BIT_XOR_ASSIGN);
    if (!r) r = consumeToken(b, LBRACE);
    if (!r) r = consumeToken(b, BIT_OR);
    if (!r) r = consumeToken(b, BIT_OR_ASSIGN);
    if (!r) r = consumeToken(b, COND_OR);
    if (!r) r = consumeToken(b, RBRACE);
    if (!r) r = consumeToken(b, TYPE_);
    if (!r) r = consumeToken(b, BREAK);
    if (!r) r = consumeToken(b, CASE);
    if (!r) r = consumeToken(b, CONST);
    if (!r) r = consumeToken(b, CONTINUE);
    if (!r) r = consumeToken(b, DEFER);
    if (!r) r = consumeToken(b, ELSE);
    if (!r) r = consumeToken(b, FLOAT);
    if (!r) r = consumeToken(b, FOR);
    if (!r) r = consumeToken(b, FN);
    if (!r) r = consumeToken(b, GO);
    if (!r) r = consumeToken(b, SPAWN);
    if (!r) r = consumeToken(b, GOTO);
    if (!r) r = consumeToken(b, HEX);
    if (!r) r = consumeToken(b, IDENTIFIER);
    if (!r) r = consumeToken(b, IF);
    if (!r) r = consumeToken(b, INT);
    if (!r) r = consumeToken(b, INTERFACE);
    if (!r) r = consumeToken(b, OCT);
    if (!r) r = consumeToken(b, RETURN);
    if (!r) r = consumeToken(b, SELECT);
    if (!r) r = consumeToken(b, RAW_STRING);
    if (!r) r = consumeToken(b, OPEN_QUOTE);
    if (!r) r = consumeToken(b, CHAR);
    if (!r) r = consumeToken(b, STRUCT);
    if (!r) r = consumeToken(b, ENUM);
    if (!r) r = consumeToken(b, UNION);
    if (!r) r = consumeToken(b, VAR);
    if (!r) r = consumeToken(b, UNSAFE);
    if (!r) r = consumeToken(b, ASSERT);
    if (!r) r = consumeToken(b, MATCH);
    if (!r) r = consumeToken(b, ASM);
    if (!r) r = consumeToken(b, TRUE);
    if (!r) r = consumeToken(b, FALSE);
    if (!r) r = consumeToken(b, NONE);
    if (!r) r = consumeToken(b, NIL);
    if (!r) r = consumeToken(b, TYPEOF);
    if (!r) r = consumeToken(b, OFFSETOF);
    if (!r) r = consumeToken(b, SIZEOF);
    if (!r) r = consumeToken(b, ISREFTYPE);
    if (!r) r = consumeToken(b, DUMP);
    return r;
  }

  /* ********************************************************** */
  // Expression !':'
  static boolean ExpressionWithRecover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ExpressionWithRecover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = Expression(b, l + 1, -1);
    r = r && ExpressionWithRecover_1(b, l + 1);
    exit_section_(b, l, m, r, false, VlangParser::ExpressionListRecover);
    return r;
  }

  // !':'
  private static boolean ExpressionWithRecover_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ExpressionWithRecover_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, COLON);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // (PlainFieldDeclaration | EmbeddedDefinition) semi?
  public static boolean FieldDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldDeclaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FIELD_DECLARATION, "<field declaration>");
    r = FieldDeclaration_0(b, l + 1);
    r = r && FieldDeclaration_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // PlainFieldDeclaration | EmbeddedDefinition
  private static boolean FieldDeclaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldDeclaration_0")) return false;
    boolean r;
    r = PlainFieldDeclaration(b, l + 1);
    if (!r) r = EmbeddedDefinition(b, l + 1);
    return r;
  }

  // semi?
  private static boolean FieldDeclaration_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldDeclaration_1")) return false;
    semi(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // FieldDeclaration+
  static boolean FieldDeclarations(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldDeclarations")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = FieldDeclaration(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!FieldDeclaration(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "FieldDeclarations", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // identifier
  public static boolean FieldDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldDefinition")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, FIELD_DEFINITION, r);
    return r;
  }

  /* ********************************************************** */
  // ReferenceExpression
  public static boolean FieldName(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldName")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ReferenceExpression(b, l + 1);
    exit_section_(b, m, FIELD_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // UnfinishedMemberModifiers | WithModifiersFieldsGroup | WithoutModifiersFieldsGroup
  public static boolean FieldsGroup(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FieldsGroup")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FIELDS_GROUP, "<fields group>");
    r = UnfinishedMemberModifiers(b, l + 1);
    if (!r) r = WithModifiersFieldsGroup(b, l + 1);
    if (!r) r = WithoutModifiersFieldsGroup(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ShebangClause? ModuleClause? ImportList? TopLevelDeclaration*
  static boolean File(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "File")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = File_0(b, l + 1);
    r = r && File_1(b, l + 1);
    r = r && File_2(b, l + 1);
    r = r && File_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ShebangClause?
  private static boolean File_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "File_0")) return false;
    ShebangClause(b, l + 1);
    return true;
  }

  // ModuleClause?
  private static boolean File_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "File_1")) return false;
    ModuleClause(b, l + 1);
    return true;
  }

  // ImportList?
  private static boolean File_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "File_2")) return false;
    ImportList(b, l + 1);
    return true;
  }

  // TopLevelDeclaration*
  private static boolean File_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "File_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!TopLevelDeclaration(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "File_3", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // Key | <<keyOrValueExpression>>
  static boolean First(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "First")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = Key(b, l + 1);
    if (!r) r = keyOrValueExpression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '[' Expression ']' Type
  public static boolean FixedSizeArrayType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FixedSizeArrayType")) return false;
    if (!nextTokenIs(b, LBRACK)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACK);
    r = r && Expression(b, l + 1, -1);
    r = r && consumeToken(b, RBRACK);
    r = r && Type(b, l + 1);
    exit_section_(b, m, FIXED_SIZE_ARRAY_TYPE, r);
    return r;
  }

  /* ********************************************************** */
  // SimpleStatement? ';' Expression? ';' (<<withOn "noBraces" SimpleStatement>>)?
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

  // (<<withOn "noBraces" SimpleStatement>>)?
  private static boolean ForClause_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ForClause_4")) return false;
    ForClause_4_0(b, l + 1);
    return true;
  }

  // <<withOn "noBraces" SimpleStatement>>
  private static boolean ForClause_4_0(PsiBuilder b, int l) {
    return withOn(b, l + 1, "noBraces", VlangParser::SimpleStatement);
  }

  /* ********************************************************** */
  // for (ForClause | RangeClause | BeforeBlockExpression)? Block
  public static boolean ForStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ForStatement")) return false;
    if (!nextTokenIs(b, FOR)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FOR_STATEMENT, null);
    r = consumeToken(b, FOR);
    p = r; // pin = 1
    r = r && report_error_(b, ForStatement_1(b, l + 1));
    r = p && Block(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (ForClause | RangeClause | BeforeBlockExpression)?
  private static boolean ForStatement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ForStatement_1")) return false;
    ForStatement_1_0(b, l + 1);
    return true;
  }

  // ForClause | RangeClause | BeforeBlockExpression
  private static boolean ForStatement_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ForStatement_1_0")) return false;
    boolean r;
    r = ForClause(b, l + 1);
    if (!r) r = RangeClause(b, l + 1);
    if (!r) r = BeforeBlockExpression(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // ':' FormatSpecifierExpression
  public static boolean FormatSpecifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FormatSpecifier")) return false;
    if (!nextTokenIs(b, COLON)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && FormatSpecifierExpression(b, l + 1);
    exit_section_(b, m, FORMAT_SPECIFIER, r);
    return r;
  }

  /* ********************************************************** */
  // (FormatSpecifierLeftAlignFlag | FormatSpecifierRightAlignFlag)? FormatSpecifierWidthAndPrecision? FormatSpecifierLetter?
  public static boolean FormatSpecifierExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FormatSpecifierExpression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FORMAT_SPECIFIER_EXPRESSION, "<format specifier expression>");
    r = FormatSpecifierExpression_0(b, l + 1);
    r = r && FormatSpecifierExpression_1(b, l + 1);
    r = r && FormatSpecifierExpression_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (FormatSpecifierLeftAlignFlag | FormatSpecifierRightAlignFlag)?
  private static boolean FormatSpecifierExpression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FormatSpecifierExpression_0")) return false;
    FormatSpecifierExpression_0_0(b, l + 1);
    return true;
  }

  // FormatSpecifierLeftAlignFlag | FormatSpecifierRightAlignFlag
  private static boolean FormatSpecifierExpression_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FormatSpecifierExpression_0_0")) return false;
    boolean r;
    r = FormatSpecifierLeftAlignFlag(b, l + 1);
    if (!r) r = FormatSpecifierRightAlignFlag(b, l + 1);
    return r;
  }

  // FormatSpecifierWidthAndPrecision?
  private static boolean FormatSpecifierExpression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FormatSpecifierExpression_1")) return false;
    FormatSpecifierWidthAndPrecision(b, l + 1);
    return true;
  }

  // FormatSpecifierLetter?
  private static boolean FormatSpecifierExpression_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FormatSpecifierExpression_2")) return false;
    FormatSpecifierLetter(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // '-'
  public static boolean FormatSpecifierLeftAlignFlag(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FormatSpecifierLeftAlignFlag")) return false;
    if (!nextTokenIs(b, MINUS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, MINUS);
    exit_section_(b, m, FORMAT_SPECIFIER_LEFT_ALIGN_FLAG, r);
    return r;
  }

  /* ********************************************************** */
  // identifier
  public static boolean FormatSpecifierLetter(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FormatSpecifierLetter")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, FORMAT_SPECIFIER_LETTER, r);
    return r;
  }

  /* ********************************************************** */
  // '+'
  public static boolean FormatSpecifierRightAlignFlag(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FormatSpecifierRightAlignFlag")) return false;
    if (!nextTokenIs(b, PLUS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PLUS);
    exit_section_(b, m, FORMAT_SPECIFIER_RIGHT_ALIGN_FLAG, r);
    return r;
  }

  /* ********************************************************** */
  // int | float
  public static boolean FormatSpecifierWidthAndPrecision(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FormatSpecifierWidthAndPrecision")) return false;
    if (!nextTokenIs(b, "<format specifier width and precision>", FLOAT, INT)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FORMAT_SPECIFIER_WIDTH_AND_PRECISION, "<format specifier width and precision>");
    r = consumeToken(b, INT);
    if (!r) r = consumeToken(b, FLOAT);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // Attributes? SymbolVisibility? fn identifier GenericParameters? Signature BlockWithConsume?
  public static boolean FunctionDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FunctionDeclaration")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_DECLARATION, "<function declaration>");
    r = FunctionDeclaration_0(b, l + 1);
    r = r && FunctionDeclaration_1(b, l + 1);
    r = r && consumeTokens(b, 2, FN, IDENTIFIER);
    p = r; // pin = 4
    r = r && report_error_(b, FunctionDeclaration_4(b, l + 1));
    r = p && report_error_(b, Signature(b, l + 1)) && r;
    r = p && FunctionDeclaration_6(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Attributes?
  private static boolean FunctionDeclaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FunctionDeclaration_0")) return false;
    Attributes(b, l + 1);
    return true;
  }

  // SymbolVisibility?
  private static boolean FunctionDeclaration_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FunctionDeclaration_1")) return false;
    SymbolVisibility(b, l + 1);
    return true;
  }

  // GenericParameters?
  private static boolean FunctionDeclaration_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FunctionDeclaration_4")) return false;
    GenericParameters(b, l + 1);
    return true;
  }

  // BlockWithConsume?
  private static boolean FunctionDeclaration_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FunctionDeclaration_6")) return false;
    BlockWithConsume(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // fn GenericParameters? Signature
  public static boolean FunctionType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FunctionType")) return false;
    if (!nextTokenIs(b, FN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_TYPE, null);
    r = consumeToken(b, FN);
    p = r; // pin = 1
    r = r && report_error_(b, FunctionType_1(b, l + 1));
    r = p && Signature(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // GenericParameters?
  private static boolean FunctionType_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FunctionType_1")) return false;
    GenericParameters(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // GenericArgumentsOld | GenericArgumentsNew
  public static boolean GenericArguments(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GenericArguments")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, GENERIC_ARGUMENTS, "<generic arguments>");
    r = GenericArgumentsOld(b, l + 1);
    if (!r) r = GenericArgumentsNew(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // <<leftBracket>> TypeListNoPin? ']'
  public static boolean GenericArgumentsNew(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GenericArgumentsNew")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, GENERIC_ARGUMENTS, "<generic arguments new>");
    r = leftBracket(b, l + 1);
    r = r && GenericArgumentsNew_1(b, l + 1);
    r = r && consumeToken(b, RBRACK);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // TypeListNoPin?
  private static boolean GenericArgumentsNew_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GenericArgumentsNew_1")) return false;
    TypeListNoPin(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // '<' TypeListNoPin? '>'
  public static boolean GenericArgumentsOld(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GenericArgumentsOld")) return false;
    if (!nextTokenIs(b, LESS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LESS);
    r = r && GenericArgumentsOld_1(b, l + 1);
    r = r && consumeToken(b, GREATER);
    exit_section_(b, m, GENERIC_ARGUMENTS, r);
    return r;
  }

  // TypeListNoPin?
  private static boolean GenericArgumentsOld_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GenericArgumentsOld_1")) return false;
    TypeListNoPin(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // identifier
  public static boolean GenericParameter(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GenericParameter")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, GENERIC_PARAMETER, r);
    return r;
  }

  /* ********************************************************** */
  // GenericParameter (',' GenericParameter)* ','?
  public static boolean GenericParameterList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GenericParameterList")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, GENERIC_PARAMETER_LIST, null);
    r = GenericParameter(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, GenericParameterList_1(b, l + 1));
    r = p && GenericParameterList_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (',' GenericParameter)*
  private static boolean GenericParameterList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GenericParameterList_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!GenericParameterList_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "GenericParameterList_1", c)) break;
    }
    return true;
  }

  // ',' GenericParameter
  private static boolean GenericParameterList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GenericParameterList_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && GenericParameter(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ','?
  private static boolean GenericParameterList_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GenericParameterList_2")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // GenericParametersOld | GenericParametersNew
  public static boolean GenericParameters(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GenericParameters")) return false;
    if (!nextTokenIs(b, "<generic parameters>", LBRACK, LESS)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, GENERIC_PARAMETERS, "<generic parameters>");
    r = GenericParametersOld(b, l + 1);
    if (!r) r = GenericParametersNew(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '[' GenericParameterList? ']'
  public static boolean GenericParametersNew(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GenericParametersNew")) return false;
    if (!nextTokenIs(b, LBRACK)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACK);
    r = r && GenericParametersNew_1(b, l + 1);
    r = r && consumeToken(b, RBRACK);
    exit_section_(b, m, GENERIC_PARAMETERS, r);
    return r;
  }

  // GenericParameterList?
  private static boolean GenericParametersNew_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GenericParametersNew_1")) return false;
    GenericParameterList(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // '<' GenericParameterList? '>'
  public static boolean GenericParametersOld(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GenericParametersOld")) return false;
    if (!nextTokenIs(b, LESS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LESS);
    r = r && GenericParametersOld_1(b, l + 1);
    r = r && consumeToken(b, GREATER);
    exit_section_(b, m, GENERIC_PARAMETERS, r);
    return r;
  }

  // GenericParameterList?
  private static boolean GenericParametersOld_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GenericParametersOld_1")) return false;
    GenericParameterList(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // Attributes? '__global' ( GlobalVariableDefinition | '(' GlobalVariableDefinitions? ')' )
  public static boolean GlobalVariableDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GlobalVariableDeclaration")) return false;
    if (!nextTokenIs(b, "<global variable declaration>", BUILTIN_GLOBAL, LBRACK)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, GLOBAL_VARIABLE_DECLARATION, "<global variable declaration>");
    r = GlobalVariableDeclaration_0(b, l + 1);
    r = r && consumeToken(b, BUILTIN_GLOBAL);
    p = r; // pin = 2
    r = r && GlobalVariableDeclaration_2(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Attributes?
  private static boolean GlobalVariableDeclaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GlobalVariableDeclaration_0")) return false;
    Attributes(b, l + 1);
    return true;
  }

  // GlobalVariableDefinition | '(' GlobalVariableDefinitions? ')'
  private static boolean GlobalVariableDeclaration_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GlobalVariableDeclaration_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = GlobalVariableDefinition(b, l + 1);
    if (!r) r = GlobalVariableDeclaration_2_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '(' GlobalVariableDefinitions? ')'
  private static boolean GlobalVariableDeclaration_2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GlobalVariableDeclaration_2_1")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, LPAREN);
    r = r && GlobalVariableDeclaration_2_1_1(b, l + 1);
    p = r; // pin = 2
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // GlobalVariableDefinitions?
  private static boolean GlobalVariableDeclaration_2_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GlobalVariableDeclaration_2_1_1")) return false;
    GlobalVariableDefinitions(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // identifier (VarModifiers Type | GlobalVariableValue)
  public static boolean GlobalVariableDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GlobalVariableDefinition")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, GLOBAL_VARIABLE_DEFINITION, null);
    r = consumeToken(b, IDENTIFIER);
    p = r; // pin = 1
    r = r && GlobalVariableDefinition_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // VarModifiers Type | GlobalVariableValue
  private static boolean GlobalVariableDefinition_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GlobalVariableDefinition_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = GlobalVariableDefinition_1_0(b, l + 1);
    if (!r) r = GlobalVariableValue(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // VarModifiers Type
  private static boolean GlobalVariableDefinition_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GlobalVariableDefinition_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = VarModifiers(b, l + 1);
    r = r && Type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // GlobalVariableDefinition (semi GlobalVariableDefinition)* semi?
  static boolean GlobalVariableDefinitions(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GlobalVariableDefinitions")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = GlobalVariableDefinition(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, GlobalVariableDefinitions_1(b, l + 1));
    r = p && GlobalVariableDefinitions_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (semi GlobalVariableDefinition)*
  private static boolean GlobalVariableDefinitions_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GlobalVariableDefinitions_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!GlobalVariableDefinitions_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "GlobalVariableDefinitions_1", c)) break;
    }
    return true;
  }

  // semi GlobalVariableDefinition
  private static boolean GlobalVariableDefinitions_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GlobalVariableDefinitions_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = semi(b, l + 1);
    r = r && GlobalVariableDefinition(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // semi?
  private static boolean GlobalVariableDefinitions_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GlobalVariableDefinitions_2")) return false;
    semi(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // '=' (<<withOff Expression "BLOCK?">> | (!() Expression))
  static boolean GlobalVariableValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GlobalVariableValue")) return false;
    if (!nextTokenIs(b, ASSIGN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, ASSIGN);
    p = r; // pin = 1
    r = r && GlobalVariableValue_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // <<withOff Expression "BLOCK?">> | (!() Expression)
  private static boolean GlobalVariableValue_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GlobalVariableValue_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = withOff(b, l + 1, Expression_parser_, "BLOCK?");
    if (!r) r = GlobalVariableValue_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !() Expression
  private static boolean GlobalVariableValue_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GlobalVariableValue_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = GlobalVariableValue_1_1_0(b, l + 1);
    r = r && Expression(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !()
  private static boolean GlobalVariableValue_1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GlobalVariableValue_1_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !GlobalVariableValue_1_1_0_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ()
  private static boolean GlobalVariableValue_1_1_0_0(PsiBuilder b, int l) {
    return true;
  }

  /* ********************************************************** */
  // goto LabelRef
  public static boolean GotoStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GotoStatement")) return false;
    if (!nextTokenIs(b, GOTO)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, GOTO);
    r = r && LabelRef(b, l + 1);
    exit_section_(b, m, GOTO_STATEMENT, r);
    return r;
  }

  /* ********************************************************** */
  // VarDefinitionList ':=' BeforeBlockExpression
  public static boolean GuardVarDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GuardVarDeclaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, GUARD_VAR_DECLARATION, "<guard var declaration>");
    r = VarDefinitionList(b, l + 1);
    r = r && consumeToken(b, VAR_ASSIGN);
    r = r && BeforeBlockExpression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // if Expression
  public static boolean IfAttribute(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IfAttribute")) return false;
    if (!nextTokenIs(b, IF)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, IF_ATTRIBUTE, null);
    r = consumeToken(b, IF);
    p = r; // pin = 1
    r = r && Expression(b, l + 1, -1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // if Condition semi? Block (semi? ElseBranch)?
  public static boolean IfExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IfExpression")) return false;
    if (!nextTokenIs(b, IF)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, IF_EXPRESSION, null);
    r = consumeToken(b, IF);
    p = r; // pin = 1
    r = r && report_error_(b, Condition(b, l + 1));
    r = p && report_error_(b, IfExpression_2(b, l + 1)) && r;
    r = p && report_error_(b, Block(b, l + 1)) && r;
    r = p && IfExpression_4(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // semi?
  private static boolean IfExpression_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IfExpression_2")) return false;
    semi(b, l + 1);
    return true;
  }

  // (semi? ElseBranch)?
  private static boolean IfExpression_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IfExpression_4")) return false;
    IfExpression_4_0(b, l + 1);
    return true;
  }

  // semi? ElseBranch
  private static boolean IfExpression_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IfExpression_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = IfExpression_4_0_0(b, l + 1);
    r = r && ElseBranch(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // semi?
  private static boolean IfExpression_4_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IfExpression_4_0_0")) return false;
    semi(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // as ImportAliasName
  public static boolean ImportAlias(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ImportAlias")) return false;
    if (!nextTokenIs(b, AS)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, IMPORT_ALIAS, null);
    r = consumeToken(b, AS);
    p = r; // pin = 1
    r = r && ImportAliasName(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // identifier
  public static boolean ImportAliasName(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ImportAliasName")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, IMPORT_ALIAS_NAME, r);
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
  // identifier
  public static boolean ImportName(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ImportName")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, IMPORT_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // ImportName ('.' ImportName)*
  public static boolean ImportPath(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ImportPath")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ImportName(b, l + 1);
    r = r && ImportPath_1(b, l + 1);
    exit_section_(b, m, IMPORT_PATH, r);
    return r;
  }

  // ('.' ImportName)*
  private static boolean ImportPath_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ImportPath_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ImportPath_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ImportPath_1", c)) break;
    }
    return true;
  }

  // '.' ImportName
  private static boolean ImportPath_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ImportPath_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DOT);
    r = r && ImportName(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ImportPath ImportAlias? SelectiveImportList?
  public static boolean ImportSpec(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ImportSpec")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ImportPath(b, l + 1);
    r = r && ImportSpec_1(b, l + 1);
    r = r && ImportSpec_2(b, l + 1);
    exit_section_(b, m, IMPORT_SPEC, r);
    return r;
  }

  // ImportAlias?
  private static boolean ImportSpec_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ImportSpec_1")) return false;
    ImportAlias(b, l + 1);
    return true;
  }

  // SelectiveImportList?
  private static boolean ImportSpec_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ImportSpec_2")) return false;
    SelectiveImportList(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // Expression SliceExprBody?
  static boolean IndexExprBody(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IndexExprBody")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = Expression(b, l + 1, -1);
    r = r && IndexExprBody_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // SliceExprBody?
  private static boolean IndexExprBody_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IndexExprBody_1")) return false;
    SliceExprBody(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // ('[' | '#[') IndexOrSliceInner ']' !identifier
  public static boolean IndexOrSliceExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IndexOrSliceExpr")) return false;
    if (!nextTokenIs(b, "<index or slice expr>", HASH_LBRACK,LBRACK)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_, INDEX_OR_SLICE_EXPR, "<index or slice expr>");
    r = IndexOrSliceExpr_0(b, l + 1);
    r = r && IndexOrSliceInner(b, l + 1);
    r = r && consumeToken(b, RBRACK);
    r = r && IndexOrSliceExpr_3(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '[' | '#['
  private static boolean IndexOrSliceExpr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IndexOrSliceExpr_0")) return false;
    boolean r;
    r = consumeToken(b, LBRACK);
    if (!r) r = consumeToken(b, HASH_LBRACK);
    return r;
  }

  // !identifier
  private static boolean IndexOrSliceExpr_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IndexOrSliceExpr_3")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, IDENTIFIER);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // EmptySlice | ((SliceExprBody | IndexExprBody) '..'?)
  static boolean IndexOrSliceInner(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IndexOrSliceInner")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = EmptySlice(b, l + 1);
    if (!r) r = IndexOrSliceInner_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (SliceExprBody | IndexExprBody) '..'?
  private static boolean IndexOrSliceInner_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IndexOrSliceInner_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = IndexOrSliceInner_1_0(b, l + 1);
    r = r && IndexOrSliceInner_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // SliceExprBody | IndexExprBody
  private static boolean IndexOrSliceInner_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IndexOrSliceInner_1_0")) return false;
    boolean r;
    r = SliceExprBody(b, l + 1);
    if (!r) r = IndexExprBody(b, l + 1);
    return r;
  }

  // '..'?
  private static boolean IndexOrSliceInner_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IndexOrSliceInner_1_1")) return false;
    consumeToken(b, RANGE);
    return true;
  }

  /* ********************************************************** */
  // Attributes? SymbolVisibility? InterfaceType
  public static boolean InterfaceDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "InterfaceDeclaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, INTERFACE_DECLARATION, "<interface declaration>");
    r = InterfaceDeclaration_0(b, l + 1);
    r = r && InterfaceDeclaration_1(b, l + 1);
    r = r && InterfaceType(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // Attributes?
  private static boolean InterfaceDeclaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "InterfaceDeclaration_0")) return false;
    Attributes(b, l + 1);
    return true;
  }

  // SymbolVisibility?
  private static boolean InterfaceDeclaration_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "InterfaceDeclaration_1")) return false;
    SymbolVisibility(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // InterfaceMethodDeclaration | FieldDeclaration | EmbeddedInterfaceDefinition
  static boolean InterfaceMember(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "InterfaceMember")) return false;
    boolean r;
    r = InterfaceMethodDeclaration(b, l + 1);
    if (!r) r = FieldDeclaration(b, l + 1);
    if (!r) r = EmbeddedInterfaceDefinition(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // InterfaceMember+
  static boolean InterfaceMembers(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "InterfaceMembers")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = InterfaceMember(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!InterfaceMember(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "InterfaceMembers", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // InterfaceMethodDefinition Attribute? DefaultFieldValue? semi
  public static boolean InterfaceMethodDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "InterfaceMethodDeclaration")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = InterfaceMethodDefinition(b, l + 1);
    r = r && InterfaceMethodDeclaration_1(b, l + 1);
    r = r && InterfaceMethodDeclaration_2(b, l + 1);
    r = r && semi(b, l + 1);
    exit_section_(b, m, INTERFACE_METHOD_DECLARATION, r);
    return r;
  }

  // Attribute?
  private static boolean InterfaceMethodDeclaration_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "InterfaceMethodDeclaration_1")) return false;
    Attribute(b, l + 1);
    return true;
  }

  // DefaultFieldValue?
  private static boolean InterfaceMethodDeclaration_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "InterfaceMethodDeclaration_2")) return false;
    DefaultFieldValue(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // identifier GenericParameters? Signature
  public static boolean InterfaceMethodDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "InterfaceMethodDefinition")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    r = r && InterfaceMethodDefinition_1(b, l + 1);
    r = r && Signature(b, l + 1);
    exit_section_(b, m, INTERFACE_METHOD_DEFINITION, r);
    return r;
  }

  // GenericParameters?
  private static boolean InterfaceMethodDefinition_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "InterfaceMethodDefinition_1")) return false;
    GenericParameters(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // interface identifier GenericParameters? '{' MembersGroup* '}'
  public static boolean InterfaceType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "InterfaceType")) return false;
    if (!nextTokenIs(b, INTERFACE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, INTERFACE_TYPE, null);
    r = consumeTokens(b, 1, INTERFACE, IDENTIFIER);
    p = r; // pin = 1
    r = r && report_error_(b, InterfaceType_2(b, l + 1));
    r = p && report_error_(b, consumeToken(b, LBRACE)) && r;
    r = p && report_error_(b, InterfaceType_4(b, l + 1)) && r;
    r = p && consumeToken(b, RBRACE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // GenericParameters?
  private static boolean InterfaceType_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "InterfaceType_2")) return false;
    GenericParameters(b, l + 1);
    return true;
  }

  // MembersGroup*
  private static boolean InterfaceType_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "InterfaceType_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!MembersGroup(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "InterfaceType_4", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // isreftype GenericArguments? '('  Expression? ')'
  public static boolean IsRefTypeCallExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IsRefTypeCallExpr")) return false;
    if (!nextTokenIs(b, ISREFTYPE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, IS_REF_TYPE_CALL_EXPR, null);
    r = consumeToken(b, ISREFTYPE);
    p = r; // pin = 1
    r = r && report_error_(b, IsRefTypeCallExpr_1(b, l + 1));
    r = p && report_error_(b, consumeToken(b, LPAREN)) && r;
    r = p && report_error_(b, IsRefTypeCallExpr_3(b, l + 1)) && r;
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // GenericArguments?
  private static boolean IsRefTypeCallExpr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IsRefTypeCallExpr_1")) return false;
    GenericArguments(b, l + 1);
    return true;
  }

  // Expression?
  private static boolean IsRefTypeCallExpr_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IsRefTypeCallExpr_3")) return false;
    Expression(b, l + 1, -1);
    return true;
  }

  /* ********************************************************** */
  // '(' <<enterMode "PAR">> Type ','? ElementList? '...'? ','? <<exitModeSafe "PAR">> ')'
  public static boolean JsonArgumentList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "JsonArgumentList")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, JSON_ARGUMENT_LIST, null);
    r = consumeToken(b, LPAREN);
    p = r; // pin = 1
    r = r && report_error_(b, enterMode(b, l + 1, "PAR"));
    r = p && report_error_(b, Type(b, l + 1)) && r;
    r = p && report_error_(b, JsonArgumentList_3(b, l + 1)) && r;
    r = p && report_error_(b, JsonArgumentList_4(b, l + 1)) && r;
    r = p && report_error_(b, JsonArgumentList_5(b, l + 1)) && r;
    r = p && report_error_(b, JsonArgumentList_6(b, l + 1)) && r;
    r = p && report_error_(b, exitModeSafe(b, l + 1, "PAR")) && r;
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ','?
  private static boolean JsonArgumentList_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "JsonArgumentList_3")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  // ElementList?
  private static boolean JsonArgumentList_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "JsonArgumentList_4")) return false;
    ElementList(b, l + 1);
    return true;
  }

  // '...'?
  private static boolean JsonArgumentList_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "JsonArgumentList_5")) return false;
    consumeToken(b, TRIPLE_DOT);
    return true;
  }

  // ','?
  private static boolean JsonArgumentList_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "JsonArgumentList_6")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // JsonArgumentList
  public static boolean JsonCallExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "JsonCallExpr")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_, JSON_CALL_EXPR, null);
    r = JsonArgumentList(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // (FieldName &':') | !() Expression
  public static boolean Key(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Key")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = Key_0(b, l + 1);
    if (!r) r = Key_1(b, l + 1);
    exit_section_(b, m, KEY, r);
    return r;
  }

  // FieldName &':'
  private static boolean Key_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Key_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = FieldName(b, l + 1);
    r = r && Key_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // &':'
  private static boolean Key_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Key_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = consumeToken(b, COLON);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // !() Expression
  private static boolean Key_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Key_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = Key_1_0(b, l + 1);
    r = r && Expression(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !()
  private static boolean Key_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Key_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !Key_1_0_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ()
  private static boolean Key_1_0_0(PsiBuilder b, int l) {
    return true;
  }

  /* ********************************************************** */
  // Expression ':' Expression
  public static boolean KeyValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "KeyValue")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, KEY_VALUE, "<key value>");
    r = Expression(b, l + 1, -1);
    r = r && consumeToken(b, COLON);
    p = r; // pin = 2
    r = r && Expression(b, l + 1, -1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // KeyValue ((semi | ',') KeyValue)* (semi | ',')?
  public static boolean KeyValues(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "KeyValues")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, KEY_VALUES, "<key values>");
    r = KeyValue(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, KeyValues_1(b, l + 1));
    r = p && KeyValues_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ((semi | ',') KeyValue)*
  private static boolean KeyValues_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "KeyValues_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!KeyValues_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "KeyValues_1", c)) break;
    }
    return true;
  }

  // (semi | ',') KeyValue
  private static boolean KeyValues_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "KeyValues_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = KeyValues_1_0_0(b, l + 1);
    r = r && KeyValue(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // semi | ','
  private static boolean KeyValues_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "KeyValues_1_0_0")) return false;
    boolean r;
    r = semi(b, l + 1);
    if (!r) r = consumeToken(b, COMMA);
    return r;
  }

  // (semi | ',')?
  private static boolean KeyValues_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "KeyValues_2")) return false;
    KeyValues_2_0(b, l + 1);
    return true;
  }

  // semi | ','
  private static boolean KeyValues_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "KeyValues_2_0")) return false;
    boolean r;
    r = semi(b, l + 1);
    if (!r) r = consumeToken(b, COMMA);
    return r;
  }

  /* ********************************************************** */
  // identifier ':'
  public static boolean LabelDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "LabelDefinition")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 2, IDENTIFIER, COLON);
    exit_section_(b, m, LABEL_DEFINITION, r);
    return r;
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

  /* ********************************************************** */
  // LabelDefinition Statement?
  public static boolean LabeledStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "LabeledStatement")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = LabelDefinition(b, l + 1);
    r = r && LabeledStatement_1(b, l + 1);
    exit_section_(b, m, LABELED_STATEMENT, r);
    return r;
  }

  // Statement?
  private static boolean LabeledStatement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "LabeledStatement_1")) return false;
    Statement(b, l + 1);
    return true;
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
  // <<braceRuleMarker>> Type <<prevIsNotFunType>> GenericArguments? '{' ElementList? '}'
  public static boolean LiteralValueExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "LiteralValueExpression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LITERAL_VALUE_EXPRESSION, "<literal value expression>");
    r = braceRuleMarker(b, l + 1);
    r = r && Type(b, l + 1);
    r = r && prevIsNotFunType(b, l + 1);
    r = r && LiteralValueExpression_3(b, l + 1);
    r = r && consumeToken(b, LBRACE);
    r = r && LiteralValueExpression_5(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // GenericArguments?
  private static boolean LiteralValueExpression_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "LiteralValueExpression_3")) return false;
    GenericArguments(b, l + 1);
    return true;
  }

  // ElementList?
  private static boolean LiteralValueExpression_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "LiteralValueExpression_5")) return false;
    ElementList(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // (lock | rlock) BeforeBlockExpressionList? ';'?
  public static boolean LockParts(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "LockParts")) return false;
    if (!nextTokenIs(b, "<lock parts>", LOCK, RLOCK)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, LOCK_PARTS, "<lock parts>");
    r = LockParts_0(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, LockParts_1(b, l + 1));
    r = p && LockParts_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // lock | rlock
  private static boolean LockParts_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "LockParts_0")) return false;
    boolean r;
    r = consumeToken(b, LOCK);
    if (!r) r = consumeToken(b, RLOCK);
    return r;
  }

  // BeforeBlockExpressionList?
  private static boolean LockParts_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "LockParts_1")) return false;
    BeforeBlockExpressionList(b, l + 1);
    return true;
  }

  // ';'?
  private static boolean LockParts_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "LockParts_2")) return false;
    consumeToken(b, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // LONG_TEMPLATE_ENTRY_START Expression semi? FormatSpecifier? TEMPLATE_ENTRY_END
  public static boolean LongStringTemplateEntry(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "LongStringTemplateEntry")) return false;
    if (!nextTokenIs(b, LONG_TEMPLATE_ENTRY_START)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, LONG_STRING_TEMPLATE_ENTRY, null);
    r = consumeToken(b, LONG_TEMPLATE_ENTRY_START);
    p = r; // pin = 1
    r = r && report_error_(b, Expression(b, l + 1, -1));
    r = p && report_error_(b, LongStringTemplateEntry_2(b, l + 1)) && r;
    r = p && report_error_(b, LongStringTemplateEntry_3(b, l + 1)) && r;
    r = p && consumeToken(b, TEMPLATE_ENTRY_END) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // semi?
  private static boolean LongStringTemplateEntry_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "LongStringTemplateEntry_2")) return false;
    semi(b, l + 1);
    return true;
  }

  // FormatSpecifier?
  private static boolean LongStringTemplateEntry_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "LongStringTemplateEntry_3")) return false;
    FormatSpecifier(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // <<prevIsNotType>> <<braceRuleMarker>> '{' <<enterMode "MAP_KEY_VALUE">> KeyValues? <<exitMode "MAP_KEY_VALUE">> '}'
  public static boolean MapInitExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MapInitExpr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MAP_INIT_EXPR, "<map init expr>");
    r = prevIsNotType(b, l + 1);
    r = r && braceRuleMarker(b, l + 1);
    r = r && consumeToken(b, LBRACE);
    r = r && enterMode(b, l + 1, "MAP_KEY_VALUE");
    r = r && MapInitExpr_4(b, l + 1);
    r = r && exitMode(b, l + 1, "MAP_KEY_VALUE");
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // KeyValues?
  private static boolean MapInitExpr_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MapInitExpr_4")) return false;
    KeyValues(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'map' '[' Type ']' Type
  public static boolean MapType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MapType")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, MAP_TYPE, "<map type>");
    r = consumeToken(b, "map");
    r = r && consumeToken(b, LBRACK);
    p = r; // pin = 2
    r = r && report_error_(b, Type(b, l + 1));
    r = p && report_error_(b, consumeToken(b, RBRACK)) && r;
    r = p && Type(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // MatchExpressionList Block semi
  public static boolean MatchArm(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MatchArm")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MATCH_ARM, "<match arm>");
    r = MatchExpressionList(b, l + 1);
    r = r && Block(b, l + 1);
    r = r && semi(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // <<typeOrExpression>>
  static boolean MatchArmExpression(PsiBuilder b, int l) {
    return typeOrExpression(b, l + 1);
  }

  /* ********************************************************** */
  // (MatchArm | MatchElseArmClause)*
  public static boolean MatchArms(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MatchArms")) return false;
    Marker m = enter_section_(b, l, _NONE_, MATCH_ARMS, "<match arms>");
    while (true) {
      int c = current_position_(b);
      if (!MatchArms_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "MatchArms", c)) break;
    }
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  // MatchArm | MatchElseArmClause
  private static boolean MatchArms_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MatchArms_0")) return false;
    boolean r;
    r = MatchArm(b, l + 1);
    if (!r) r = MatchElseArmClause(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // else Block semi
  public static boolean MatchElseArmClause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MatchElseArmClause")) return false;
    if (!nextTokenIs(b, ELSE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, MATCH_ELSE_ARM_CLAUSE, null);
    r = consumeToken(b, ELSE);
    p = r; // pin = 1
    r = r && report_error_(b, Block(b, l + 1));
    r = p && semi(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // MatchArmExpression (',' MatchArmExpression)*
  static boolean MatchExpressionList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MatchExpressionList")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = MatchArmExpression(b, l + 1);
    p = r; // pin = 1
    r = r && MatchExpressionList_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (',' MatchArmExpression)*
  private static boolean MatchExpressionList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MatchExpressionList_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!MatchExpressionList_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "MatchExpressionList_1", c)) break;
    }
    return true;
  }

  // ',' MatchArmExpression
  private static boolean MatchExpressionList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MatchExpressionList_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && MatchArmExpression(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // mut | pub | '__global'
  public static boolean MemberModifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MemberModifier")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MEMBER_MODIFIER, "<member modifier>");
    r = consumeToken(b, MUT);
    if (!r) r = consumeToken(b, PUB);
    if (!r) r = consumeToken(b, BUILTIN_GLOBAL);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // MemberModifier* ':'
  public static boolean MemberModifiers(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MemberModifiers")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MEMBER_MODIFIERS, "<member modifiers>");
    r = MemberModifiers_0(b, l + 1);
    r = r && consumeToken(b, COLON);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // MemberModifier*
  private static boolean MemberModifiers_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MemberModifiers_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!MemberModifier(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "MemberModifiers_0", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // WithModifiersMembersGroup | WithoutModifiersMemberGroup
  public static boolean MembersGroup(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MembersGroup")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MEMBERS_GROUP, "<members group>");
    r = WithModifiersMembersGroup(b, l + 1);
    if (!r) r = WithoutModifiersMemberGroup(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // Attributes? SymbolVisibility? fn '(' Receiver ')' MethodName GenericParameters? Signature BlockWithConsume?
  public static boolean MethodDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MethodDeclaration")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, METHOD_DECLARATION, "<method declaration>");
    r = MethodDeclaration_0(b, l + 1);
    r = r && MethodDeclaration_1(b, l + 1);
    r = r && consumeTokens(b, 0, FN, LPAREN);
    r = r && Receiver(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    r = r && MethodName(b, l + 1);
    p = r; // pin = 7
    r = r && report_error_(b, MethodDeclaration_7(b, l + 1));
    r = p && report_error_(b, Signature(b, l + 1)) && r;
    r = p && MethodDeclaration_9(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Attributes?
  private static boolean MethodDeclaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MethodDeclaration_0")) return false;
    Attributes(b, l + 1);
    return true;
  }

  // SymbolVisibility?
  private static boolean MethodDeclaration_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MethodDeclaration_1")) return false;
    SymbolVisibility(b, l + 1);
    return true;
  }

  // GenericParameters?
  private static boolean MethodDeclaration_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MethodDeclaration_7")) return false;
    GenericParameters(b, l + 1);
    return true;
  }

  // BlockWithConsume?
  private static boolean MethodDeclaration_9(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MethodDeclaration_9")) return false;
    BlockWithConsume(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // identifier | AddOp | MulOp | RelOp
  public static boolean MethodName(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MethodName")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, METHOD_NAME, "<method name>");
    r = consumeToken(b, IDENTIFIER);
    if (!r) r = AddOp(b, l + 1);
    if (!r) r = MulOp(b, l + 1);
    if (!r) r = RelOp(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
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

  /* ********************************************************** */
  // '*' | '/' | '%' | <<gtGtGt>> | <<gtGt>> | '&' | '&^'
  static boolean MulOp(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MulOp")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, MUL);
    if (!r) r = consumeToken(b, QUOTIENT);
    if (!r) r = consumeToken(b, REMAINDER);
    if (!r) r = gtGtGt(b, l + 1);
    if (!r) r = gtGt(b, l + 1);
    if (!r) r = consumeToken(b, BIT_AND);
    if (!r) r = consumeToken(b, BIT_CLEAR);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // none
  public static boolean NoneType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "NoneType")) return false;
    if (!nextTokenIs(b, NONE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, NONE);
    exit_section_(b, m, NONE_TYPE, r);
    return r;
  }

  /* ********************************************************** */
  // offsetof '(' Type ',' ReferenceExpression ')'
  public static boolean OffsetOfCallExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "OffsetOfCallExpr")) return false;
    if (!nextTokenIs(b, OFFSETOF)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, OFFSET_OF_CALL_EXPR, null);
    r = consumeTokens(b, 1, OFFSETOF, LPAREN);
    p = r; // pin = 1
    r = r && report_error_(b, Type(b, l + 1));
    r = p && report_error_(b, consumeToken(b, COMMA)) && r;
    r = p && report_error_(b, ReferenceExpression(b, l + 1)) && r;
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // '?'
  public static boolean OptionPropagationExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "OptionPropagationExpression")) return false;
    if (!nextTokenIs(b, QUESTION)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, QUESTION);
    exit_section_(b, m, OPTION_PROPAGATION_EXPRESSION, r);
    return r;
  }

  /* ********************************************************** */
  // '?' Type?
  public static boolean OptionType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "OptionType")) return false;
    if (!nextTokenIs(b, QUESTION)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, OPTION_TYPE, null);
    r = consumeToken(b, QUESTION);
    p = r; // pin = 1
    r = r && OptionType_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Type?
  private static boolean OptionType_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "OptionType_1")) return false;
    Type(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // or Block
  public static boolean OrBlockExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "OrBlockExpr")) return false;
    if (!nextTokenIs(b, OR)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_, OR_BLOCK_EXPR, null);
    r = consumeToken(b, OR);
    r = r && Block(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ParamWithName | ParamWithoutName
  public static boolean ParamDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParamDefinition")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PARAM_DEFINITION, "<param definition>");
    r = ParamWithName(b, l + 1);
    if (!r) r = ParamWithoutName(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // VarModifiers? identifier '...'? Type
  static boolean ParamWithName(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParamWithName")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ParamWithName_0(b, l + 1);
    r = r && consumeToken(b, IDENTIFIER);
    r = r && ParamWithName_2(b, l + 1);
    r = r && Type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // VarModifiers?
  private static boolean ParamWithName_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParamWithName_0")) return false;
    VarModifiers(b, l + 1);
    return true;
  }

  // '...'?
  private static boolean ParamWithName_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParamWithName_2")) return false;
    consumeToken(b, TRIPLE_DOT);
    return true;
  }

  /* ********************************************************** */
  // VarModifiers? '...'? Type
  static boolean ParamWithoutName(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParamWithoutName")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ParamWithoutName_0(b, l + 1);
    r = r && ParamWithoutName_1(b, l + 1);
    r = r && Type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // VarModifiers?
  private static boolean ParamWithoutName_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParamWithoutName_0")) return false;
    VarModifiers(b, l + 1);
    return true;
  }

  // '...'?
  private static boolean ParamWithoutName_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParamWithoutName_1")) return false;
    consumeToken(b, TRIPLE_DOT);
    return true;
  }

  /* ********************************************************** */
  // ParamDefinition (',' (ParamDefinition | &')'))* list_separator?
  static boolean ParameterList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParameterList")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = ParamDefinition(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, ParameterList_1(b, l + 1));
    r = p && ParameterList_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (',' (ParamDefinition | &')'))*
  private static boolean ParameterList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParameterList_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ParameterList_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ParameterList_1", c)) break;
    }
    return true;
  }

  // ',' (ParamDefinition | &')')
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

  // ParamDefinition | &')'
  private static boolean ParameterList_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParameterList_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ParamDefinition(b, l + 1);
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

  // list_separator?
  private static boolean ParameterList_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParameterList_2")) return false;
    list_separator(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // '(' ParameterList? ','? ')'
  public static boolean Parameters(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Parameters")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PARAMETERS, null);
    r = consumeToken(b, LPAREN);
    p = r; // pin = 1
    r = r && report_error_(b, Parameters_1(b, l + 1));
    r = p && report_error_(b, Parameters_2(b, l + 1)) && r;
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ParameterList?
  private static boolean Parameters_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Parameters_1")) return false;
    ParameterList(b, l + 1);
    return true;
  }

  // ','?
  private static boolean Parameters_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Parameters_2")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // AttributeKey AttributeValueWithColon?
  public static boolean PlainAttribute(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PlainAttribute")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PLAIN_ATTRIBUTE, "<plain attribute>");
    r = AttributeKey(b, l + 1);
    r = r && PlainAttribute_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // AttributeValueWithColon?
  private static boolean PlainAttribute_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PlainAttribute_1")) return false;
    AttributeValueWithColon(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // FieldDefinition Type Attribute? DefaultFieldValue?
  static boolean PlainFieldDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PlainFieldDeclaration")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = FieldDefinition(b, l + 1);
    r = r && Type(b, l + 1);
    r = r && PlainFieldDeclaration_2(b, l + 1);
    r = r && PlainFieldDeclaration_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // Attribute?
  private static boolean PlainFieldDeclaration_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PlainFieldDeclaration_2")) return false;
    Attribute(b, l + 1);
    return true;
  }

  // DefaultFieldValue?
  private static boolean PlainFieldDeclaration_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PlainFieldDeclaration_3")) return false;
    DefaultFieldValue(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // ('&' | '&&')+ Type
  public static boolean PointerType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PointerType")) return false;
    if (!nextTokenIs(b, "<pointer type>", BIT_AND, COND_AND)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _COLLAPSE_, POINTER_TYPE, "<pointer type>");
    r = PointerType_0(b, l + 1);
    p = r; // pin = 1
    r = r && Type(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ('&' | '&&')+
  private static boolean PointerType_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PointerType_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = PointerType_0_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!PointerType_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "PointerType_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // '&' | '&&'
  private static boolean PointerType_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PointerType_0_0")) return false;
    boolean r;
    r = consumeToken(b, BIT_AND);
    if (!r) r = consumeToken(b, COND_AND);
    return r;
  }

  /* ********************************************************** */
  // semi? '.' identifier <<checkNoColonIfMap>>
  public static boolean QualifiedReferenceExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "QualifiedReferenceExpression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_, REFERENCE_EXPRESSION, "<qualified reference expression>");
    r = QualifiedReferenceExpression_0(b, l + 1);
    r = r && consumeTokens(b, 0, DOT, IDENTIFIER);
    r = r && checkNoColonIfMap(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // semi?
  private static boolean QualifiedReferenceExpression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "QualifiedReferenceExpression_0")) return false;
    semi(b, l + 1);
    return true;
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
  // VarDefinitionList in BeforeBlockExpression
  public static boolean RangeClause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RangeClause")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, RANGE_CLAUSE, "<range clause>");
    r = VarDefinitionList(b, l + 1);
    r = r && consumeToken(b, IN);
    p = r; // pin = 2
    r = r && BeforeBlockExpression(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // VarModifiers? identifier Type ','?
  public static boolean Receiver(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Receiver")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, RECEIVER, "<receiver>");
    r = Receiver_0(b, l + 1);
    r = r && consumeToken(b, IDENTIFIER);
    p = r; // pin = 2
    r = r && report_error_(b, Type(b, l + 1));
    r = p && Receiver_3(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // VarModifiers?
  private static boolean Receiver_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Receiver_0")) return false;
    VarModifiers(b, l + 1);
    return true;
  }

  // ','?
  private static boolean Receiver_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Receiver_3")) return false;
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
  // '==' | '!=' | '<' | '<=' | '>' !'>' | '>='
  static boolean RelOp(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RelOp")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EQ);
    if (!r) r = consumeToken(b, NOT_EQ);
    if (!r) r = consumeToken(b, LESS);
    if (!r) r = consumeToken(b, LESS_OR_EQUAL);
    if (!r) r = RelOp_4(b, l + 1);
    if (!r) r = consumeToken(b, GREATER_OR_EQUAL);
    exit_section_(b, m, null, r);
    return r;
  }

  // '>' !'>'
  private static boolean RelOp_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RelOp_4")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, GREATER);
    r = r && RelOp_4_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !'>'
  private static boolean RelOp_4_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RelOp_4_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, GREATER);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // Type
  public static boolean Result(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Result")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, RESULT, "<result>");
    r = Type(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '!'
  public static boolean ResultPropagationExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ResultPropagationExpression")) return false;
    if (!nextTokenIs(b, NOT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, NOT);
    exit_section_(b, m, RESULT_PROPAGATION_EXPRESSION, r);
    return r;
  }

  /* ********************************************************** */
  // '!' Type?
  public static boolean ResultType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ResultType")) return false;
    if (!nextTokenIs(b, NOT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, RESULT_TYPE, null);
    r = consumeToken(b, NOT);
    p = r; // pin = 1
    r = r && ResultType_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Type?
  private static boolean ResultType_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ResultType_1")) return false;
    Type(b, l + 1);
    return true;
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

  // ExpressionList?
  private static boolean ReturnStatement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ReturnStatement_1")) return false;
    ExpressionList(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // CompileTimeFieldReference
  //   | (<<callExprWithPropagate>> | (!() CallExprWithPropagate))
  //   | (<<callExpr>> | (!() CallExpr))
  //   | IndexOrSliceExpr
  //   | SafeQualifiedReferenceExpression
  //   | QualifiedReferenceExpression
  //   | OrBlockExpr
  //   | OptionPropagationExpression
  //   | ResultPropagationExpression
  static boolean RightHandExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RightHandExpr")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = CompileTimeFieldReference(b, l + 1);
    if (!r) r = RightHandExpr_1(b, l + 1);
    if (!r) r = RightHandExpr_2(b, l + 1);
    if (!r) r = IndexOrSliceExpr(b, l + 1);
    if (!r) r = SafeQualifiedReferenceExpression(b, l + 1);
    if (!r) r = QualifiedReferenceExpression(b, l + 1);
    if (!r) r = OrBlockExpr(b, l + 1);
    if (!r) r = OptionPropagationExpression(b, l + 1);
    if (!r) r = ResultPropagationExpression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // <<callExprWithPropagate>> | (!() CallExprWithPropagate)
  private static boolean RightHandExpr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RightHandExpr_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = callExprWithPropagate(b, l + 1);
    if (!r) r = RightHandExpr_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !() CallExprWithPropagate
  private static boolean RightHandExpr_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RightHandExpr_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = RightHandExpr_1_1_0(b, l + 1);
    r = r && CallExprWithPropagate(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !()
  private static boolean RightHandExpr_1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RightHandExpr_1_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !RightHandExpr_1_1_0_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ()
  private static boolean RightHandExpr_1_1_0_0(PsiBuilder b, int l) {
    return true;
  }

  // <<callExpr>> | (!() CallExpr)
  private static boolean RightHandExpr_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RightHandExpr_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = callExpr(b, l + 1);
    if (!r) r = RightHandExpr_2_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !() CallExpr
  private static boolean RightHandExpr_2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RightHandExpr_2_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = RightHandExpr_2_1_0(b, l + 1);
    r = r && CallExpr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !()
  private static boolean RightHandExpr_2_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RightHandExpr_2_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !RightHandExpr_2_1_0_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ()
  private static boolean RightHandExpr_2_1_0_0(PsiBuilder b, int l) {
    return true;
  }

  /* ********************************************************** */
  // RightHandExpr*
  static boolean RightHandExprs(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RightHandExprs")) return false;
    while (true) {
      int c = current_position_(b);
      if (!RightHandExpr(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "RightHandExprs", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // semi? '?.' identifier
  public static boolean SafeQualifiedReferenceExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SafeQualifiedReferenceExpression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_, REFERENCE_EXPRESSION, "<safe qualified reference expression>");
    r = SafeQualifiedReferenceExpression_0(b, l + 1);
    r = r && consumeTokens(b, 0, SAFE_DOT, IDENTIFIER);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // semi?
  private static boolean SafeQualifiedReferenceExpression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SafeQualifiedReferenceExpression_0")) return false;
    semi(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // SelectArmStatement Block semi
  public static boolean SelectArm(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SelectArm")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SELECT_ARM, "<select arm>");
    r = SelectArmStatement(b, l + 1);
    r = r && Block(b, l + 1);
    p = r; // pin = 2
    r = r && semi(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // AssignOp BeforeBlockExpression
  public static boolean SelectArmAssignmentStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SelectArmAssignmentStatement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_, SELECT_ARM_ASSIGNMENT_STATEMENT, "<select arm assignment statement>");
    r = AssignOp(b, l + 1);
    r = r && BeforeBlockExpression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // (<<withOn "noBraces" VarDeclaration>>)
  //   | SendStatement
  //   | <<withOn "noBraces" LeftHandExprList>> SelectArmAssignmentStatement?
  public static boolean SelectArmStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SelectArmStatement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, SELECT_ARM_STATEMENT, "<select arm statement>");
    r = SelectArmStatement_0(b, l + 1);
    if (!r) r = SendStatement(b, l + 1);
    if (!r) r = SelectArmStatement_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // <<withOn "noBraces" VarDeclaration>>
  private static boolean SelectArmStatement_0(PsiBuilder b, int l) {
    return withOn(b, l + 1, "noBraces", VlangParser::VarDeclaration);
  }

  // <<withOn "noBraces" LeftHandExprList>> SelectArmAssignmentStatement?
  private static boolean SelectArmStatement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SelectArmStatement_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = withOn(b, l + 1, "noBraces", VlangParser::LeftHandExprList);
    r = r && SelectArmStatement_2_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // SelectArmAssignmentStatement?
  private static boolean SelectArmStatement_2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SelectArmStatement_2_1")) return false;
    SelectArmAssignmentStatement(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // (SelectArm | SelectElseArmClause)*
  public static boolean SelectArms(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SelectArms")) return false;
    Marker m = enter_section_(b, l, _NONE_, SELECT_ARMS, "<select arms>");
    while (true) {
      int c = current_position_(b);
      if (!SelectArms_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "SelectArms", c)) break;
    }
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  // SelectArm | SelectElseArmClause
  private static boolean SelectArms_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SelectArms_0")) return false;
    boolean r;
    r = SelectArm(b, l + 1);
    if (!r) r = SelectElseArmClause(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // else Block semi
  public static boolean SelectElseArmClause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SelectElseArmClause")) return false;
    if (!nextTokenIs(b, ELSE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SELECT_ELSE_ARM_CLAUSE, null);
    r = consumeToken(b, ELSE);
    p = r; // pin = 1
    r = r && report_error_(b, Block(b, l + 1));
    r = p && semi(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // '{' ReferenceExpression (list_separator ReferenceExpression)* list_separator? '}'
  public static boolean SelectiveImportList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SelectiveImportList")) return false;
    if (!nextTokenIs(b, LBRACE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SELECTIVE_IMPORT_LIST, null);
    r = consumeToken(b, LBRACE);
    r = r && ReferenceExpression(b, l + 1);
    p = r; // pin = 2
    r = r && report_error_(b, SelectiveImportList_2(b, l + 1));
    r = p && report_error_(b, SelectiveImportList_3(b, l + 1)) && r;
    r = p && consumeToken(b, RBRACE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (list_separator ReferenceExpression)*
  private static boolean SelectiveImportList_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SelectiveImportList_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!SelectiveImportList_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "SelectiveImportList_2", c)) break;
    }
    return true;
  }

  // list_separator ReferenceExpression
  private static boolean SelectiveImportList_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SelectiveImportList_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = list_separator(b, l + 1);
    r = r && ReferenceExpression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // list_separator?
  private static boolean SelectiveImportList_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SelectiveImportList_3")) return false;
    list_separator(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // '<-' BeforeBlockExpression
  public static boolean SendStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SendStatement")) return false;
    if (!nextTokenIs(b, SEND_CHANNEL)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _LEFT_, SEND_STATEMENT, null);
    r = consumeToken(b, SEND_CHANNEL);
    p = r; // pin = 1
    r = r && BeforeBlockExpression(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // shared Type
  public static boolean SharedType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SharedType")) return false;
    if (!nextTokenIs(b, SHARED)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SHARED_TYPE, null);
    r = consumeToken(b, SHARED);
    p = r; // pin = 1
    r = r && Type(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // shebang
  public static boolean ShebangClause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShebangClause")) return false;
    if (!nextTokenIs(b, SHEBANG)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SHEBANG);
    exit_section_(b, m, SHEBANG_CLAUSE, r);
    return r;
  }

  /* ********************************************************** */
  // <<shiftLeft>>
  public static boolean ShiftLeftOp(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShiftLeftOp")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SHIFT_LEFT_OP, "<shift left op>");
    r = shiftLeft(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // SHORT_TEMPLATE_ENTRY_START Expression
  public static boolean ShortStringTemplateEntry(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShortStringTemplateEntry")) return false;
    if (!nextTokenIs(b, SHORT_TEMPLATE_ENTRY_START)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SHORT_TEMPLATE_ENTRY_START);
    r = r && Expression(b, l + 1, -1);
    exit_section_(b, m, SHORT_STRING_TEMPLATE_ENTRY, r);
    return r;
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
  // VarDeclaration
  //   | (LeftHandExprList AssignmentStatement? | SendStatement)
  public static boolean SimpleStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SimpleStatement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, SIMPLE_STATEMENT, "<simple statement>");
    r = VarDeclaration(b, l + 1);
    if (!r) r = SimpleStatement_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // LeftHandExprList AssignmentStatement? | SendStatement
  private static boolean SimpleStatement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SimpleStatement_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = SimpleStatement_1_0(b, l + 1);
    if (!r) r = SendStatement(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // LeftHandExprList AssignmentStatement?
  private static boolean SimpleStatement_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SimpleStatement_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = LeftHandExprList(b, l + 1);
    p = r; // pin = LeftHandExprList
    r = r && SimpleStatement_1_0_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // AssignmentStatement?
  private static boolean SimpleStatement_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SimpleStatement_1_0_1")) return false;
    AssignmentStatement(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // sizeof GenericArguments? '('  Expression? ')'
  public static boolean SizeOfCallExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SizeOfCallExpr")) return false;
    if (!nextTokenIs(b, SIZEOF)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SIZE_OF_CALL_EXPR, null);
    r = consumeToken(b, SIZEOF);
    p = r; // pin = 1
    r = r && report_error_(b, SizeOfCallExpr_1(b, l + 1));
    r = p && report_error_(b, consumeToken(b, LPAREN)) && r;
    r = p && report_error_(b, SizeOfCallExpr_3(b, l + 1)) && r;
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // GenericArguments?
  private static boolean SizeOfCallExpr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SizeOfCallExpr_1")) return false;
    GenericArguments(b, l + 1);
    return true;
  }

  // Expression?
  private static boolean SizeOfCallExpr_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SizeOfCallExpr_3")) return false;
    Expression(b, l + 1, -1);
    return true;
  }

  /* ********************************************************** */
  // '..' Expression
  static boolean SliceExprBody(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SliceExprBody")) return false;
    if (!nextTokenIs(b, RANGE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, RANGE);
    r = r && Expression(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '{' ('}' | (SqlBlockStatement semi)* '}')
  public static boolean SqlBlock(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlBlock")) return false;
    if (!nextTokenIs(b, LBRACE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SQL_BLOCK, null);
    r = consumeToken(b, LBRACE);
    p = r; // pin = 1
    r = r && SqlBlock_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // '}' | (SqlBlockStatement semi)* '}'
  private static boolean SqlBlock_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlBlock_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, RBRACE);
    if (!r) r = SqlBlock_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (SqlBlockStatement semi)* '}'
  private static boolean SqlBlock_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlBlock_1_1")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = SqlBlock_1_1_0(b, l + 1);
    p = r; // pin = 1
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (SqlBlockStatement semi)*
  private static boolean SqlBlock_1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlBlock_1_1_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!SqlBlock_1_1_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "SqlBlock_1_1_0", c)) break;
    }
    return true;
  }

  // SqlBlockStatement semi
  private static boolean SqlBlock_1_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlBlock_1_1_0_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = SqlBlockStatement(b, l + 1);
    p = r; // pin = 1
    r = r && semi(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // SqlCreateStatement
  //   | SqlDropStatement
  //   | SqlSelectStatement
  //   | SqlUpdateStatement
  //   | SqlInsertStatement
  //   | SqlDeleteStatement
  //   | identifier
  public static boolean SqlBlockStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlBlockStatement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, SQL_BLOCK_STATEMENT, "<sql block statement>");
    r = SqlCreateStatement(b, l + 1);
    if (!r) r = SqlDropStatement(b, l + 1);
    if (!r) r = SqlSelectStatement(b, l + 1);
    if (!r) r = SqlUpdateStatement(b, l + 1);
    if (!r) r = SqlInsertStatement(b, l + 1);
    if (!r) r = SqlDeleteStatement(b, l + 1);
    if (!r) r = consumeToken(b, IDENTIFIER);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // semi? 'create' 'table' SqlTableName
  public static boolean SqlCreateStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlCreateStatement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SQL_CREATE_STATEMENT, "<sql create statement>");
    r = SqlCreateStatement_0(b, l + 1);
    r = r && consumeToken(b, "create");
    p = r; // pin = 2
    r = r && report_error_(b, consumeToken(b, "table"));
    r = p && SqlTableName(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // semi?
  private static boolean SqlCreateStatement_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlCreateStatement_0")) return false;
    semi(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // semi? 'delete' ((ReferenceExpression SqlFromClause) | SqlFromClause) SqlWhereClause?
  public static boolean SqlDeleteStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlDeleteStatement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SQL_DELETE_STATEMENT, "<sql delete statement>");
    r = SqlDeleteStatement_0(b, l + 1);
    r = r && consumeToken(b, "delete");
    p = r; // pin = 2
    r = r && report_error_(b, SqlDeleteStatement_2(b, l + 1));
    r = p && SqlDeleteStatement_3(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // semi?
  private static boolean SqlDeleteStatement_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlDeleteStatement_0")) return false;
    semi(b, l + 1);
    return true;
  }

  // (ReferenceExpression SqlFromClause) | SqlFromClause
  private static boolean SqlDeleteStatement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlDeleteStatement_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = SqlDeleteStatement_2_0(b, l + 1);
    if (!r) r = SqlFromClause(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ReferenceExpression SqlFromClause
  private static boolean SqlDeleteStatement_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlDeleteStatement_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ReferenceExpression(b, l + 1);
    r = r && SqlFromClause(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // SqlWhereClause?
  private static boolean SqlDeleteStatement_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlDeleteStatement_3")) return false;
    SqlWhereClause(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // semi? 'drop' 'table' SqlTableName
  public static boolean SqlDropStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlDropStatement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SQL_DROP_STATEMENT, "<sql drop statement>");
    r = SqlDropStatement_0(b, l + 1);
    r = r && consumeToken(b, "drop");
    p = r; // pin = 2
    r = r && report_error_(b, consumeToken(b, "table"));
    r = p && SqlTableName(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // semi?
  private static boolean SqlDropStatement_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlDropStatement_0")) return false;
    semi(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'sql' BeforeBlockExpression SqlBlock
  public static boolean SqlExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlExpression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SQL_EXPRESSION, "<sql expression>");
    r = consumeToken(b, "sql");
    r = r && BeforeBlockExpression(b, l + 1);
    r = r && SqlBlock(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // semi? 'from' SqlTableName
  public static boolean SqlFromClause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlFromClause")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SQL_FROM_CLAUSE, "<sql from clause>");
    r = SqlFromClause_0(b, l + 1);
    r = r && consumeToken(b, "from");
    p = r; // pin = 2
    r = r && SqlTableName(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // semi?
  private static boolean SqlFromClause_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlFromClause_0")) return false;
    semi(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // semi? 'insert' ReferenceExpression semi? 'into' SqlTableName
  public static boolean SqlInsertStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlInsertStatement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SQL_INSERT_STATEMENT, "<sql insert statement>");
    r = SqlInsertStatement_0(b, l + 1);
    r = r && consumeToken(b, "insert");
    p = r; // pin = 2
    r = r && report_error_(b, ReferenceExpression(b, l + 1));
    r = p && report_error_(b, SqlInsertStatement_3(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, "into")) && r;
    r = p && SqlTableName(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // semi?
  private static boolean SqlInsertStatement_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlInsertStatement_0")) return false;
    semi(b, l + 1);
    return true;
  }

  // semi?
  private static boolean SqlInsertStatement_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlInsertStatement_3")) return false;
    semi(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'limit' Expression
  public static boolean SqlLimitClause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlLimitClause")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SQL_LIMIT_CLAUSE, "<sql limit clause>");
    r = consumeToken(b, "limit");
    r = r && Expression(b, l + 1, -1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'offset' Expression
  public static boolean SqlOffsetClause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlOffsetClause")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SQL_OFFSET_CLAUSE, "<sql offset clause>");
    r = consumeToken(b, "offset");
    r = r && Expression(b, l + 1, -1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // semi?  'order' 'by' SqlReferenceList? SqlOrderByTypes?
  public static boolean SqlOrderByClause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlOrderByClause")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SQL_ORDER_BY_CLAUSE, "<sql order by clause>");
    r = SqlOrderByClause_0(b, l + 1);
    r = r && consumeToken(b, "order");
    p = r; // pin = 2
    r = r && report_error_(b, consumeToken(b, "by"));
    r = p && report_error_(b, SqlOrderByClause_3(b, l + 1)) && r;
    r = p && SqlOrderByClause_4(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // semi?
  private static boolean SqlOrderByClause_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlOrderByClause_0")) return false;
    semi(b, l + 1);
    return true;
  }

  // SqlReferenceList?
  private static boolean SqlOrderByClause_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlOrderByClause_3")) return false;
    SqlReferenceList(b, l + 1);
    return true;
  }

  // SqlOrderByTypes?
  private static boolean SqlOrderByClause_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlOrderByClause_4")) return false;
    SqlOrderByTypes(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'asc' | 'desc'
  static boolean SqlOrderByTypes(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlOrderByTypes")) return false;
    boolean r;
    r = consumeToken(b, "asc");
    if (!r) r = consumeToken(b, "desc");
    return r;
  }

  /* ********************************************************** */
  // semi? SqlReferenceListItem (','? SqlReferenceListItem)*
  public static boolean SqlReferenceList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlReferenceList")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SQL_REFERENCE_LIST, "<sql reference list>");
    r = SqlReferenceList_0(b, l + 1);
    r = r && SqlReferenceListItem(b, l + 1);
    p = r; // pin = 2
    r = r && SqlReferenceList_2(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // semi?
  private static boolean SqlReferenceList_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlReferenceList_0")) return false;
    semi(b, l + 1);
    return true;
  }

  // (','? SqlReferenceListItem)*
  private static boolean SqlReferenceList_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlReferenceList_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!SqlReferenceList_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "SqlReferenceList_2", c)) break;
    }
    return true;
  }

  // ','? SqlReferenceListItem
  private static boolean SqlReferenceList_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlReferenceList_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = SqlReferenceList_2_0_0(b, l + 1);
    r = r && SqlReferenceListItem(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ','?
  private static boolean SqlReferenceList_2_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlReferenceList_2_0_0")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // semi? <<endOfLimit>> ReferenceExpression
  public static boolean SqlReferenceListItem(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlReferenceListItem")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SQL_REFERENCE_LIST_ITEM, "<sql reference list item>");
    r = SqlReferenceListItem_0(b, l + 1);
    r = r && endOfLimit(b, l + 1);
    r = r && ReferenceExpression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // semi?
  private static boolean SqlReferenceListItem_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlReferenceListItem_0")) return false;
    semi(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // semi? 'count'
  public static boolean SqlSelectCountClause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlSelectCountClause")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SQL_SELECT_COUNT_CLAUSE, "<sql select count clause>");
    r = SqlSelectCountClause_0(b, l + 1);
    r = r && consumeToken(b, "count");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // semi?
  private static boolean SqlSelectCountClause_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlSelectCountClause_0")) return false;
    semi(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // semi? 'select' SqlSelectCountClause? SqlFromClause SqlWhereClause? SqlOrderByClause? SqlLimitClause? SqlOffsetClause?
  public static boolean SqlSelectStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlSelectStatement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SQL_SELECT_STATEMENT, "<sql select statement>");
    r = SqlSelectStatement_0(b, l + 1);
    r = r && consumeToken(b, "select");
    p = r; // pin = 2
    r = r && report_error_(b, SqlSelectStatement_2(b, l + 1));
    r = p && report_error_(b, SqlFromClause(b, l + 1)) && r;
    r = p && report_error_(b, SqlSelectStatement_4(b, l + 1)) && r;
    r = p && report_error_(b, SqlSelectStatement_5(b, l + 1)) && r;
    r = p && report_error_(b, SqlSelectStatement_6(b, l + 1)) && r;
    r = p && SqlSelectStatement_7(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // semi?
  private static boolean SqlSelectStatement_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlSelectStatement_0")) return false;
    semi(b, l + 1);
    return true;
  }

  // SqlSelectCountClause?
  private static boolean SqlSelectStatement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlSelectStatement_2")) return false;
    SqlSelectCountClause(b, l + 1);
    return true;
  }

  // SqlWhereClause?
  private static boolean SqlSelectStatement_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlSelectStatement_4")) return false;
    SqlWhereClause(b, l + 1);
    return true;
  }

  // SqlOrderByClause?
  private static boolean SqlSelectStatement_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlSelectStatement_5")) return false;
    SqlOrderByClause(b, l + 1);
    return true;
  }

  // SqlLimitClause?
  private static boolean SqlSelectStatement_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlSelectStatement_6")) return false;
    SqlLimitClause(b, l + 1);
    return true;
  }

  // SqlOffsetClause?
  private static boolean SqlSelectStatement_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlSelectStatement_7")) return false;
    SqlOffsetClause(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // TypeName
  public static boolean SqlTableName(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlTableName")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = TypeName(b, l + 1);
    exit_section_(b, m, SQL_TABLE_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // ReferenceExpression '=' Expression
  public static boolean SqlUpdateItem(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlUpdateItem")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ReferenceExpression(b, l + 1);
    r = r && consumeToken(b, ASSIGN);
    r = r && Expression(b, l + 1, -1);
    exit_section_(b, m, SQL_UPDATE_ITEM, r);
    return r;
  }

  /* ********************************************************** */
  // SqlUpdateItem (',' SqlUpdateItem)*
  public static boolean SqlUpdateList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlUpdateList")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SQL_UPDATE_LIST, null);
    r = SqlUpdateItem(b, l + 1);
    p = r; // pin = 1
    r = r && SqlUpdateList_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (',' SqlUpdateItem)*
  private static boolean SqlUpdateList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlUpdateList_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!SqlUpdateList_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "SqlUpdateList_1", c)) break;
    }
    return true;
  }

  // ',' SqlUpdateItem
  private static boolean SqlUpdateList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlUpdateList_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && SqlUpdateItem(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // semi? 'update' SqlTableName semi? 'set' SqlUpdateList SqlWhereClause
  public static boolean SqlUpdateStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlUpdateStatement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SQL_UPDATE_STATEMENT, "<sql update statement>");
    r = SqlUpdateStatement_0(b, l + 1);
    r = r && consumeToken(b, "update");
    p = r; // pin = 2
    r = r && report_error_(b, SqlTableName(b, l + 1));
    r = p && report_error_(b, SqlUpdateStatement_3(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, "set")) && r;
    r = p && report_error_(b, SqlUpdateList(b, l + 1)) && r;
    r = p && SqlWhereClause(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // semi?
  private static boolean SqlUpdateStatement_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlUpdateStatement_0")) return false;
    semi(b, l + 1);
    return true;
  }

  // semi?
  private static boolean SqlUpdateStatement_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlUpdateStatement_3")) return false;
    semi(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // semi? 'where' Expression
  public static boolean SqlWhereClause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlWhereClause")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SQL_WHERE_CLAUSE, "<sql where clause>");
    r = SqlWhereClause_0(b, l + 1);
    r = r && consumeToken(b, "where");
    p = r; // pin = 2
    r = r && Expression(b, l + 1, -1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // semi?
  private static boolean SqlWhereClause_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SqlWhereClause_0")) return false;
    semi(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // BlockNoPin
  //   | AppendStatement
  //   | SimpleStatement
  //   | ReturnStatement
  //   | BreakStatement
  //   | ContinueStatement
  //   | GotoStatement
  //   | ForStatement
  //   | CompileTimeForStatement
  //   | AssertStatement
  //   | AsmBlockStatement
  //   | LabeledStatement
  //   | DeferStatement
  public static boolean Statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, STATEMENT, "<statement>");
    r = BlockNoPin(b, l + 1);
    if (!r) r = AppendStatement(b, l + 1);
    if (!r) r = SimpleStatement(b, l + 1);
    if (!r) r = ReturnStatement(b, l + 1);
    if (!r) r = BreakStatement(b, l + 1);
    if (!r) r = ContinueStatement(b, l + 1);
    if (!r) r = GotoStatement(b, l + 1);
    if (!r) r = ForStatement(b, l + 1);
    if (!r) r = CompileTimeForStatement(b, l + 1);
    if (!r) r = AssertStatement(b, l + 1);
    if (!r) r = AsmBlockStatement(b, l + 1);
    if (!r) r = LabeledStatement(b, l + 1);
    if (!r) r = DeferStatement(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // !('!' | '?' | '&' | '[' | '(' | '*' | '+' | '-' | ';' | '<-' | '^' | '{' | '|' | '|=' | '||' | '&&' | '}' | type | break | case | const | continue | defer | else | float | for | fn | pub | mut | volatile | shared | go | spawn | goto | hex | identifier | if | int | interface | oct | return | select | 'raw_string' | OPEN_QUOTE | char | struct | union | var | unsafe | assert | match | lock | rlock | asm | true | false | none | '$for' | '$if' | '$else' | '__global' | typeof | offsetof | sizeof | isreftype | dump | nil)
  static boolean StatementRecover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StatementRecover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !StatementRecover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '!' | '?' | '&' | '[' | '(' | '*' | '+' | '-' | ';' | '<-' | '^' | '{' | '|' | '|=' | '||' | '&&' | '}' | type | break | case | const | continue | defer | else | float | for | fn | pub | mut | volatile | shared | go | spawn | goto | hex | identifier | if | int | interface | oct | return | select | 'raw_string' | OPEN_QUOTE | char | struct | union | var | unsafe | assert | match | lock | rlock | asm | true | false | none | '$for' | '$if' | '$else' | '__global' | typeof | offsetof | sizeof | isreftype | dump | nil
  private static boolean StatementRecover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StatementRecover_0")) return false;
    boolean r;
    r = consumeToken(b, NOT);
    if (!r) r = consumeToken(b, QUESTION);
    if (!r) r = consumeToken(b, BIT_AND);
    if (!r) r = consumeToken(b, LBRACK);
    if (!r) r = consumeToken(b, LPAREN);
    if (!r) r = consumeToken(b, MUL);
    if (!r) r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, MINUS);
    if (!r) r = consumeToken(b, SEMICOLON);
    if (!r) r = consumeToken(b, SEND_CHANNEL);
    if (!r) r = consumeToken(b, BIT_XOR);
    if (!r) r = consumeToken(b, LBRACE);
    if (!r) r = consumeToken(b, BIT_OR);
    if (!r) r = consumeToken(b, BIT_OR_ASSIGN);
    if (!r) r = consumeToken(b, COND_OR);
    if (!r) r = consumeToken(b, COND_AND);
    if (!r) r = consumeToken(b, RBRACE);
    if (!r) r = consumeToken(b, TYPE);
    if (!r) r = consumeToken(b, BREAK);
    if (!r) r = consumeToken(b, CASE);
    if (!r) r = consumeToken(b, CONST);
    if (!r) r = consumeToken(b, CONTINUE);
    if (!r) r = consumeToken(b, DEFER);
    if (!r) r = consumeToken(b, ELSE);
    if (!r) r = consumeToken(b, FLOAT);
    if (!r) r = consumeToken(b, FOR);
    if (!r) r = consumeToken(b, FN);
    if (!r) r = consumeToken(b, PUB);
    if (!r) r = consumeToken(b, MUT);
    if (!r) r = consumeToken(b, VOLATILE);
    if (!r) r = consumeToken(b, SHARED);
    if (!r) r = consumeToken(b, GO);
    if (!r) r = consumeToken(b, SPAWN);
    if (!r) r = consumeToken(b, GOTO);
    if (!r) r = consumeToken(b, HEX);
    if (!r) r = consumeToken(b, IDENTIFIER);
    if (!r) r = consumeToken(b, IF);
    if (!r) r = consumeToken(b, INT);
    if (!r) r = consumeToken(b, INTERFACE);
    if (!r) r = consumeToken(b, OCT);
    if (!r) r = consumeToken(b, RETURN);
    if (!r) r = consumeToken(b, SELECT);
    if (!r) r = consumeToken(b, RAW_STRING);
    if (!r) r = consumeToken(b, OPEN_QUOTE);
    if (!r) r = consumeToken(b, CHAR);
    if (!r) r = consumeToken(b, STRUCT);
    if (!r) r = consumeToken(b, UNION);
    if (!r) r = consumeToken(b, VAR);
    if (!r) r = consumeToken(b, UNSAFE);
    if (!r) r = consumeToken(b, ASSERT);
    if (!r) r = consumeToken(b, MATCH);
    if (!r) r = consumeToken(b, LOCK);
    if (!r) r = consumeToken(b, RLOCK);
    if (!r) r = consumeToken(b, ASM);
    if (!r) r = consumeToken(b, TRUE);
    if (!r) r = consumeToken(b, FALSE);
    if (!r) r = consumeToken(b, NONE);
    if (!r) r = consumeToken(b, FOR_COMPILE_TIME);
    if (!r) r = consumeToken(b, IF_COMPILE_TIME);
    if (!r) r = consumeToken(b, ELSE_COMPILE_TIME);
    if (!r) r = consumeToken(b, BUILTIN_GLOBAL);
    if (!r) r = consumeToken(b, TYPEOF);
    if (!r) r = consumeToken(b, OFFSETOF);
    if (!r) r = consumeToken(b, SIZEOF);
    if (!r) r = consumeToken(b, ISREFTYPE);
    if (!r) r = consumeToken(b, DUMP);
    if (!r) r = consumeToken(b, NIL);
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
  // 'raw_string' | StringTemplate
  public static boolean StringLiteral(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StringLiteral")) return false;
    if (!nextTokenIs(b, "<string literal>", OPEN_QUOTE, RAW_STRING)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STRING_LITERAL, "<string literal>");
    r = consumeToken(b, RAW_STRING);
    if (!r) r = StringTemplate(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // OPEN_QUOTE StringTemplatePart* CLOSING_QUOTE
  public static boolean StringTemplate(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StringTemplate")) return false;
    if (!nextTokenIs(b, OPEN_QUOTE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, STRING_TEMPLATE, null);
    r = consumeToken(b, OPEN_QUOTE);
    p = r; // pin = 1
    r = r && report_error_(b, StringTemplate_1(b, l + 1));
    r = p && consumeToken(b, CLOSING_QUOTE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // StringTemplatePart*
  private static boolean StringTemplate_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StringTemplate_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!StringTemplatePart(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "StringTemplate_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // TEMPLATE_ENTRY_START Expression FormatSpecifier? TEMPLATE_ENTRY_END
  public static boolean StringTemplateEntry(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StringTemplateEntry")) return false;
    if (!nextTokenIs(b, TEMPLATE_ENTRY_START)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, STRING_TEMPLATE_ENTRY, null);
    r = consumeToken(b, TEMPLATE_ENTRY_START);
    p = r; // pin = 1
    r = r && report_error_(b, Expression(b, l + 1, -1));
    r = p && report_error_(b, StringTemplateEntry_2(b, l + 1)) && r;
    r = p && consumeToken(b, TEMPLATE_ENTRY_END) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // FormatSpecifier?
  private static boolean StringTemplateEntry_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StringTemplateEntry_2")) return false;
    FormatSpecifier(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // LITERAL_STRING_TEMPLATE_ENTRY
  //   | LITERAL_STRING_TEMPLATE_ESCAPE_ENTRY
  //   | LongStringTemplateEntry
  //   | ShortStringTemplateEntry
  //   | StringTemplateEntry
  //   | SHORT_TEMPLATE_ENTRY_START
  static boolean StringTemplatePart(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StringTemplatePart")) return false;
    boolean r;
    r = consumeToken(b, LITERAL_STRING_TEMPLATE_ENTRY);
    if (!r) r = consumeToken(b, LITERAL_STRING_TEMPLATE_ESCAPE_ENTRY);
    if (!r) r = LongStringTemplateEntry(b, l + 1);
    if (!r) r = ShortStringTemplateEntry(b, l + 1);
    if (!r) r = StringTemplateEntry(b, l + 1);
    if (!r) r = consumeToken(b, SHORT_TEMPLATE_ENTRY_START);
    return r;
  }

  /* ********************************************************** */
  // Attributes? SymbolVisibility? StructType
  public static boolean StructDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StructDeclaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STRUCT_DECLARATION, "<struct declaration>");
    r = StructDeclaration_0(b, l + 1);
    r = r && StructDeclaration_1(b, l + 1);
    r = r && StructType(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // Attributes?
  private static boolean StructDeclaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StructDeclaration_0")) return false;
    Attributes(b, l + 1);
    return true;
  }

  // SymbolVisibility?
  private static boolean StructDeclaration_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StructDeclaration_1")) return false;
    SymbolVisibility(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // (struct | union) identifier GenericParameters? '{'  FieldsGroup* '}'
  public static boolean StructType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StructType")) return false;
    if (!nextTokenIs(b, "<struct type>", STRUCT, UNION)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, STRUCT_TYPE, "<struct type>");
    r = StructType_0(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, consumeToken(b, IDENTIFIER));
    r = p && report_error_(b, StructType_2(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, LBRACE)) && r;
    r = p && report_error_(b, StructType_4(b, l + 1)) && r;
    r = p && consumeToken(b, RBRACE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // struct | union
  private static boolean StructType_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StructType_0")) return false;
    boolean r;
    r = consumeToken(b, STRUCT);
    if (!r) r = consumeToken(b, UNION);
    return r;
  }

  // GenericParameters?
  private static boolean StructType_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StructType_2")) return false;
    GenericParameters(b, l + 1);
    return true;
  }

  // FieldsGroup*
  private static boolean StructType_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StructType_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!FieldsGroup(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "StructType_4", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // pub | '__global'
  public static boolean SymbolVisibility(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SymbolVisibility")) return false;
    if (!nextTokenIs(b, "<symbol visibility>", BUILTIN_GLOBAL, PUB)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SYMBOL_VISIBILITY, "<symbol visibility>");
    r = consumeToken(b, PUB);
    if (!r) r = consumeToken(b, BUILTIN_GLOBAL);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'thread' Type?
  public static boolean ThreadType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ThreadType")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, THREAD_TYPE, "<thread type>");
    r = consumeToken(b, "thread");
    p = r; // pin = 1
    r = r && ThreadType_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Type?
  private static boolean ThreadType_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ThreadType_1")) return false;
    Type(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // ConstDeclaration
  //   | FunctionDeclaration
  //   | MethodDeclaration
  //   | StructDeclaration
  //   | EnumDeclaration
  //   | InterfaceDeclaration
  //   | GlobalVariableDeclaration
  //   | CompileTimeForStatement
  //   | TypeAliasDeclaration
  //   | Statement
  static boolean TopDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TopDeclaration")) return false;
    boolean r;
    r = ConstDeclaration(b, l + 1);
    if (!r) r = FunctionDeclaration(b, l + 1);
    if (!r) r = MethodDeclaration(b, l + 1);
    if (!r) r = StructDeclaration(b, l + 1);
    if (!r) r = EnumDeclaration(b, l + 1);
    if (!r) r = InterfaceDeclaration(b, l + 1);
    if (!r) r = GlobalVariableDeclaration(b, l + 1);
    if (!r) r = CompileTimeForStatement(b, l + 1);
    if (!r) r = TypeAliasDeclaration(b, l + 1);
    if (!r) r = Statement(b, l + 1);
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

  /* ********************************************************** */
  // !('type' | '__global' | enum | import | '$for' | '$if' | '[' | '!' | '?' | '&' | '(' | '*' | '+' | '-' | ';' | '<-' | '^' | '{' | '|' | '|=' | '||' | '&&' | '}' | break | case | const | continue | defer | else | float | for | fn | pub | mut | volatile | shared | go | spawn | goto | hex | identifier | if | int | interface | oct | return | select | 'raw_string' | OPEN_QUOTE | char | struct | union | var | unsafe | assert | match | lock | rlock | asm | true | false | none | '$else' | typeof | offsetof | sizeof | isreftype | dump | nil)
  static boolean TopLevelDeclarationRecover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TopLevelDeclarationRecover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !TopLevelDeclarationRecover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // 'type' | '__global' | enum | import | '$for' | '$if' | '[' | '!' | '?' | '&' | '(' | '*' | '+' | '-' | ';' | '<-' | '^' | '{' | '|' | '|=' | '||' | '&&' | '}' | break | case | const | continue | defer | else | float | for | fn | pub | mut | volatile | shared | go | spawn | goto | hex | identifier | if | int | interface | oct | return | select | 'raw_string' | OPEN_QUOTE | char | struct | union | var | unsafe | assert | match | lock | rlock | asm | true | false | none | '$else' | typeof | offsetof | sizeof | isreftype | dump | nil
  private static boolean TopLevelDeclarationRecover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TopLevelDeclarationRecover_0")) return false;
    boolean r;
    r = consumeToken(b, TYPE_);
    if (!r) r = consumeToken(b, BUILTIN_GLOBAL);
    if (!r) r = consumeToken(b, ENUM);
    if (!r) r = consumeToken(b, IMPORT);
    if (!r) r = consumeToken(b, FOR_COMPILE_TIME);
    if (!r) r = consumeToken(b, IF_COMPILE_TIME);
    if (!r) r = consumeToken(b, LBRACK);
    if (!r) r = consumeToken(b, NOT);
    if (!r) r = consumeToken(b, QUESTION);
    if (!r) r = consumeToken(b, BIT_AND);
    if (!r) r = consumeToken(b, LPAREN);
    if (!r) r = consumeToken(b, MUL);
    if (!r) r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, MINUS);
    if (!r) r = consumeToken(b, SEMICOLON);
    if (!r) r = consumeToken(b, SEND_CHANNEL);
    if (!r) r = consumeToken(b, BIT_XOR);
    if (!r) r = consumeToken(b, LBRACE);
    if (!r) r = consumeToken(b, BIT_OR);
    if (!r) r = consumeToken(b, BIT_OR_ASSIGN);
    if (!r) r = consumeToken(b, COND_OR);
    if (!r) r = consumeToken(b, COND_AND);
    if (!r) r = consumeToken(b, RBRACE);
    if (!r) r = consumeToken(b, BREAK);
    if (!r) r = consumeToken(b, CASE);
    if (!r) r = consumeToken(b, CONST);
    if (!r) r = consumeToken(b, CONTINUE);
    if (!r) r = consumeToken(b, DEFER);
    if (!r) r = consumeToken(b, ELSE);
    if (!r) r = consumeToken(b, FLOAT);
    if (!r) r = consumeToken(b, FOR);
    if (!r) r = consumeToken(b, FN);
    if (!r) r = consumeToken(b, PUB);
    if (!r) r = consumeToken(b, MUT);
    if (!r) r = consumeToken(b, VOLATILE);
    if (!r) r = consumeToken(b, SHARED);
    if (!r) r = consumeToken(b, GO);
    if (!r) r = consumeToken(b, SPAWN);
    if (!r) r = consumeToken(b, GOTO);
    if (!r) r = consumeToken(b, HEX);
    if (!r) r = consumeToken(b, IDENTIFIER);
    if (!r) r = consumeToken(b, IF);
    if (!r) r = consumeToken(b, INT);
    if (!r) r = consumeToken(b, INTERFACE);
    if (!r) r = consumeToken(b, OCT);
    if (!r) r = consumeToken(b, RETURN);
    if (!r) r = consumeToken(b, SELECT);
    if (!r) r = consumeToken(b, RAW_STRING);
    if (!r) r = consumeToken(b, OPEN_QUOTE);
    if (!r) r = consumeToken(b, CHAR);
    if (!r) r = consumeToken(b, STRUCT);
    if (!r) r = consumeToken(b, UNION);
    if (!r) r = consumeToken(b, VAR);
    if (!r) r = consumeToken(b, UNSAFE);
    if (!r) r = consumeToken(b, ASSERT);
    if (!r) r = consumeToken(b, MATCH);
    if (!r) r = consumeToken(b, LOCK);
    if (!r) r = consumeToken(b, RLOCK);
    if (!r) r = consumeToken(b, ASM);
    if (!r) r = consumeToken(b, TRUE);
    if (!r) r = consumeToken(b, FALSE);
    if (!r) r = consumeToken(b, NONE);
    if (!r) r = consumeToken(b, ELSE_COMPILE_TIME);
    if (!r) r = consumeToken(b, TYPEOF);
    if (!r) r = consumeToken(b, OFFSETOF);
    if (!r) r = consumeToken(b, SIZEOF);
    if (!r) r = consumeToken(b, ISREFTYPE);
    if (!r) r = consumeToken(b, DUMP);
    if (!r) r = consumeToken(b, NIL);
    return r;
  }

  /* ********************************************************** */
  // '(' TypeListNoPin ')'
  public static boolean TupleType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TupleType")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && TypeListNoPin(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, TUPLE_TYPE, r);
    return r;
  }

  /* ********************************************************** */
  // TypeModifiers? (TypeLit | TypeName) GenericArguments?
  public static boolean Type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, TYPE, "<type>");
    r = Type_0(b, l + 1);
    r = r && Type_1(b, l + 1);
    r = r && Type_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // TypeModifiers?
  private static boolean Type_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Type_0")) return false;
    TypeModifiers(b, l + 1);
    return true;
  }

  // TypeLit | TypeName
  private static boolean Type_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Type_1")) return false;
    boolean r;
    r = TypeLit(b, l + 1);
    if (!r) r = TypeName(b, l + 1);
    return r;
  }

  // GenericArguments?
  private static boolean Type_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Type_2")) return false;
    GenericArguments(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // Attributes? SymbolVisibility? 'type' AliasType
  public static boolean TypeAliasDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeAliasDeclaration")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, TYPE_ALIAS_DECLARATION, "<type alias declaration>");
    r = TypeAliasDeclaration_0(b, l + 1);
    r = r && TypeAliasDeclaration_1(b, l + 1);
    r = r && consumeToken(b, TYPE_);
    p = r; // pin = 3
    r = r && AliasType(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Attributes?
  private static boolean TypeAliasDeclaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeAliasDeclaration_0")) return false;
    Attributes(b, l + 1);
    return true;
  }

  // SymbolVisibility?
  private static boolean TypeAliasDeclaration_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeAliasDeclaration_1")) return false;
    SymbolVisibility(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // Type (',' Type)* ','?
  public static boolean TypeListNoPin(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeListNoPin")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TYPE_LIST_NO_PIN, "<type list no pin>");
    r = Type(b, l + 1);
    r = r && TypeListNoPin_1(b, l + 1);
    r = r && TypeListNoPin_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (',' Type)*
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
  // MapType
  //   | ArrayType
  //   | FixedSizeArrayType
  //   | TupleType
  //   | SharedType
  //   | NoneType
  //   | PointerType | WrongPointerType
  //   | OptionType
  //   | ResultType
  //   | FunctionType
  //   | ChannelType
  //   | ThreadType
  //   | AtomicType
  //   | AnonymousStructType
  //   | StructType
  //   | InterfaceType
  static boolean TypeLit(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeLit")) return false;
    boolean r;
    r = MapType(b, l + 1);
    if (!r) r = ArrayType(b, l + 1);
    if (!r) r = FixedSizeArrayType(b, l + 1);
    if (!r) r = TupleType(b, l + 1);
    if (!r) r = SharedType(b, l + 1);
    if (!r) r = NoneType(b, l + 1);
    if (!r) r = PointerType(b, l + 1);
    if (!r) r = WrongPointerType(b, l + 1);
    if (!r) r = OptionType(b, l + 1);
    if (!r) r = ResultType(b, l + 1);
    if (!r) r = FunctionType(b, l + 1);
    if (!r) r = ChannelType(b, l + 1);
    if (!r) r = ThreadType(b, l + 1);
    if (!r) r = AtomicType(b, l + 1);
    if (!r) r = AnonymousStructType(b, l + 1);
    if (!r) r = StructType(b, l + 1);
    if (!r) r = InterfaceType(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // mut | volatile | static
  public static boolean TypeModifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeModifier")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TYPE_MODIFIER, "<type modifier>");
    r = consumeToken(b, MUT);
    if (!r) r = consumeToken(b, VOLATILE);
    if (!r) r = consumeToken(b, STATIC);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // TypeModifier*
  public static boolean TypeModifiers(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeModifiers")) return false;
    Marker m = enter_section_(b, l, _NONE_, TYPE_MODIFIERS, "<type modifiers>");
    while (true) {
      int c = current_position_(b);
      if (!TypeModifier(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "TypeModifiers", c)) break;
    }
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // TypeReferenceExpression QualifiedTypeReferenceExpression*
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

  // QualifiedTypeReferenceExpression*
  private static boolean TypeName_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeName_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!QualifiedTypeReferenceExpression(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "TypeName_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // typeof GenericArguments? '(' Expression? ')'
  public static boolean TypeOfCallExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeOfCallExpr")) return false;
    if (!nextTokenIs(b, TYPEOF)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, TYPE_OF_CALL_EXPR, null);
    r = consumeToken(b, TYPEOF);
    p = r; // pin = 1
    r = r && report_error_(b, TypeOfCallExpr_1(b, l + 1));
    r = p && report_error_(b, consumeToken(b, LPAREN)) && r;
    r = p && report_error_(b, TypeOfCallExpr_3(b, l + 1)) && r;
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // GenericArguments?
  private static boolean TypeOfCallExpr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeOfCallExpr_1")) return false;
    GenericArguments(b, l + 1);
    return true;
  }

  // Expression?
  private static boolean TypeOfCallExpr_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeOfCallExpr_3")) return false;
    Expression(b, l + 1, -1);
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
  // Type (semi? '|' Type)*
  public static boolean TypeUnionList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeUnionList")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, TYPE_UNION_LIST, "<type union list>");
    r = Type(b, l + 1);
    p = r; // pin = 1
    r = r && TypeUnionList_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (semi? '|' Type)*
  private static boolean TypeUnionList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeUnionList_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!TypeUnionList_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "TypeUnionList_1", c)) break;
    }
    return true;
  }

  // semi? '|' Type
  private static boolean TypeUnionList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeUnionList_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = TypeUnionList_1_0_0(b, l + 1);
    r = r && consumeToken(b, BIT_OR);
    r = r && Type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // semi?
  private static boolean TypeUnionList_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeUnionList_1_0_0")) return false;
    semi(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // '+' | '-' | '!' | '^' | '~' | '*' | '&' | '&&' | '<-'
  static boolean UnaryOp(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "UnaryOp")) return false;
    boolean r;
    r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, MINUS);
    if (!r) r = consumeToken(b, NOT);
    if (!r) r = consumeToken(b, BIT_XOR);
    if (!r) r = consumeToken(b, TILDA);
    if (!r) r = consumeToken(b, MUL);
    if (!r) r = consumeToken(b, BIT_AND);
    if (!r) r = consumeToken(b, COND_AND);
    if (!r) r = consumeToken(b, SEND_CHANNEL);
    return r;
  }

  /* ********************************************************** */
  // MemberModifier+ !(':' | semi)
  public static boolean UnfinishedMemberModifiers(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "UnfinishedMemberModifiers")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, UNFINISHED_MEMBER_MODIFIERS, "<unfinished member modifiers>");
    r = UnfinishedMemberModifiers_0(b, l + 1);
    r = r && UnfinishedMemberModifiers_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // MemberModifier+
  private static boolean UnfinishedMemberModifiers_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "UnfinishedMemberModifiers_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = MemberModifier(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!MemberModifier(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "UnfinishedMemberModifiers_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // !(':' | semi)
  private static boolean UnfinishedMemberModifiers_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "UnfinishedMemberModifiers_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !UnfinishedMemberModifiers_1_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ':' | semi
  private static boolean UnfinishedMemberModifiers_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "UnfinishedMemberModifiers_1_0")) return false;
    boolean r;
    r = consumeToken(b, COLON);
    if (!r) r = semi(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // unsafe Block
  public static boolean UnsafeExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "UnsafeExpression")) return false;
    if (!nextTokenIs(b, UNSAFE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, UNSAFE_EXPRESSION, null);
    r = consumeToken(b, UNSAFE);
    p = r; // pin = 1
    r = r && Block(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // Expression
  public static boolean Value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VALUE, "<value>");
    r = Expression(b, l + 1, -1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // VarDefinitionList ':=' ExpressionList
  public static boolean VarDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VarDeclaration")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, VAR_DECLARATION, "<var declaration>");
    r = VarDefinitionList(b, l + 1);
    r = r && consumeToken(b, VAR_ASSIGN);
    p = r; // pin = 2
    r = r && ExpressionList(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // VarModifiers? identifier
  public static boolean VarDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VarDefinition")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VAR_DEFINITION, "<var definition>");
    r = VarDefinition_0(b, l + 1);
    r = r && consumeToken(b, IDENTIFIER);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // VarModifiers?
  private static boolean VarDefinition_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VarDefinition_0")) return false;
    VarModifiers(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // VarDefinition (',' VarDefinition)*
  static boolean VarDefinitionList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VarDefinitionList")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = VarDefinition(b, l + 1);
    p = r; // pin = 1
    r = r && VarDefinitionList_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (',' VarDefinition)*
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
  // mut | shared | volatile | static
  public static boolean VarModifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VarModifier")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VAR_MODIFIER, "<var modifier>");
    r = consumeToken(b, MUT);
    if (!r) r = consumeToken(b, SHARED);
    if (!r) r = consumeToken(b, VOLATILE);
    if (!r) r = consumeToken(b, STATIC);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // VarModifier*
  public static boolean VarModifiers(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VarModifiers")) return false;
    Marker m = enter_section_(b, l, _NONE_, VAR_MODIFIERS, "<var modifiers>");
    while (true) {
      int c = current_position_(b);
      if (!VarModifier(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "VarModifiers", c)) break;
    }
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // MemberModifiers semi? FieldDeclarations
  static boolean WithModifiersFieldsGroup(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "WithModifiersFieldsGroup")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = MemberModifiers(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, WithModifiersFieldsGroup_1(b, l + 1));
    r = p && FieldDeclarations(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // semi?
  private static boolean WithModifiersFieldsGroup_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "WithModifiersFieldsGroup_1")) return false;
    semi(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // MemberModifiers semi? InterfaceMembers
  static boolean WithModifiersMembersGroup(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "WithModifiersMembersGroup")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = MemberModifiers(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, WithModifiersMembersGroup_1(b, l + 1));
    r = p && InterfaceMembers(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // semi?
  private static boolean WithModifiersMembersGroup_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "WithModifiersMembersGroup_1")) return false;
    semi(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // !MemberModifiers FieldDeclarations
  static boolean WithoutModifiersFieldsGroup(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "WithoutModifiersFieldsGroup")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = WithoutModifiersFieldsGroup_0(b, l + 1);
    r = r && FieldDeclarations(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !MemberModifiers
  private static boolean WithoutModifiersFieldsGroup_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "WithoutModifiersFieldsGroup_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !MemberModifiers(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // !MemberModifiers InterfaceMembers
  static boolean WithoutModifiersMemberGroup(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "WithoutModifiersMemberGroup")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = WithoutModifiersMemberGroup_0(b, l + 1);
    r = r && InterfaceMembers(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !MemberModifiers
  private static boolean WithoutModifiersMemberGroup_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "WithoutModifiersMemberGroup_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !MemberModifiers(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '*'+ Type
  public static boolean WrongPointerType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "WrongPointerType")) return false;
    if (!nextTokenIs(b, MUL)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, WRONG_POINTER_TYPE, null);
    r = WrongPointerType_0(b, l + 1);
    p = r; // pin = 1
    r = r && Type(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // '*'+
  private static boolean WrongPointerType_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "WrongPointerType_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, MUL);
    while (r) {
      int c = current_position_(b);
      if (!consumeToken(b, MUL)) break;
      if (!empty_element_parsed_guard_(b, "WrongPointerType_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // semi | ','
  static boolean list_separator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_separator")) return false;
    boolean r;
    r = semi(b, l + 1);
    if (!r) r = consumeToken(b, COMMA);
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
  // 3: BINARY(RangeExpr)
  // 4: BINARY(AddExpr)
  // 5: BINARY(ShiftLeftExpr)
  // 6: BINARY(MulExpr)
  // 7: PREFIX(UnaryExpr)
  // 8: BINARY(SendExpr)
  // 9: ATOM(MatchExpression)
  // 10: ATOM(SelectExpression)
  // 11: ATOM(CompileTimeIfExpression)
  // 12: BINARY(InExpression)
  // 13: BINARY(NotInExpression)
  // 14: POSTFIX(IsExpression)
  // 15: POSTFIX(NotIsExpression)
  // 16: POSTFIX(AsExpression)
  // 17: ATOM(DotExpression) ATOM(Literal) ATOM(FunctionLit)
  // 18: ATOM(EnumFetch)
  // 19: PREFIX(MutExpression)
  // 20: PREFIX(SharedExpression)
  // 21: PREFIX(GoExpression)
  // 22: PREFIX(SpawnExpression)
  // 23: ATOM(LockExpression)
  // 24: POSTFIX(IncDecExpression)
  // 25: ATOM(UnpackingExpression)
  // 26: PREFIX(TypeCastExpression)
  // 27: ATOM(ParenthesesExpr)
  public static boolean Expression(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "Expression")) return false;
    addVariant(b, "<expression>");
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<expression>");
    r = UnaryExpr(b, l + 1);
    if (!r) r = MatchExpression(b, l + 1);
    if (!r) r = SelectExpression(b, l + 1);
    if (!r) r = CompileTimeIfExpression(b, l + 1);
    if (!r) r = DotExpression(b, l + 1);
    if (!r) r = Literal(b, l + 1);
    if (!r) r = FunctionLit(b, l + 1);
    if (!r) r = EnumFetch(b, l + 1);
    if (!r) r = MutExpression(b, l + 1);
    if (!r) r = SharedExpression(b, l + 1);
    if (!r) r = GoExpression(b, l + 1);
    if (!r) r = SpawnExpression(b, l + 1);
    if (!r) r = LockExpression(b, l + 1);
    if (!r) r = UnpackingExpression(b, l + 1);
    if (!r) r = TypeCastExpression(b, l + 1);
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
      if (g < 0 && OrExpr_0(b, l + 1)) {
        r = Expression(b, l, 0);
        exit_section_(b, l, m, OR_EXPR, r, true, null);
      }
      else if (g < 1 && AndExpr_0(b, l + 1)) {
        r = Expression(b, l, 1);
        exit_section_(b, l, m, AND_EXPR, r, true, null);
      }
      else if (g < 2 && RelOp(b, l + 1)) {
        r = Expression(b, l, 2);
        exit_section_(b, l, m, CONDITIONAL_EXPR, r, true, null);
      }
      else if (g < 3 && RangeExpr_0(b, l + 1)) {
        r = Expression(b, l, 3);
        exit_section_(b, l, m, RANGE_EXPR, r, true, null);
      }
      else if (g < 4 && AddOp(b, l + 1)) {
        r = Expression(b, l, 4);
        exit_section_(b, l, m, ADD_EXPR, r, true, null);
      }
      else if (g < 5 && ShiftLeftOp(b, l + 1)) {
        r = Expression(b, l, 5);
        exit_section_(b, l, m, SHIFT_LEFT_EXPR, r, true, null);
      }
      else if (g < 6 && MulOp(b, l + 1)) {
        r = Expression(b, l, 6);
        exit_section_(b, l, m, MUL_EXPR, r, true, null);
      }
      else if (g < 8 && consumeTokenSmart(b, SEND_CHANNEL)) {
        r = Expression(b, l, 8);
        exit_section_(b, l, m, SEND_EXPR, r, true, null);
      }
      else if (g < 12 && consumeTokenSmart(b, IN)) {
        r = Expression(b, l, 12);
        exit_section_(b, l, m, IN_EXPRESSION, r, true, null);
      }
      else if (g < 13 && consumeTokenSmart(b, NOT_IN)) {
        r = Expression(b, l, 13);
        exit_section_(b, l, m, NOT_IN_EXPRESSION, r, true, null);
      }
      else if (g < 14 && IsExpression_0(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, IS_EXPRESSION, r, true, null);
      }
      else if (g < 15 && NotIsExpression_0(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, NOT_IS_EXPRESSION, r, true, null);
      }
      else if (g < 16 && AsExpression_0(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, AS_EXPRESSION, r, true, null);
      }
      else if (g < 24 && IncDecExpression_0(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, INC_DEC_EXPRESSION, r, true, null);
      }
      else {
        exit_section_(b, l, m, null, false, false, null);
        break;
      }
    }
    return r;
  }

  // semi? '||'
  private static boolean OrExpr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "OrExpr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = OrExpr_0_0(b, l + 1);
    r = r && consumeToken(b, COND_OR);
    exit_section_(b, m, null, r);
    return r;
  }

  // semi?
  private static boolean OrExpr_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "OrExpr_0_0")) return false;
    semi(b, l + 1);
    return true;
  }

  // semi? '&&'
  private static boolean AndExpr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AndExpr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = AndExpr_0_0(b, l + 1);
    r = r && consumeToken(b, COND_AND);
    exit_section_(b, m, null, r);
    return r;
  }

  // semi?
  private static boolean AndExpr_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AndExpr_0_0")) return false;
    semi(b, l + 1);
    return true;
  }

  // ('..' | '...') !']'
  private static boolean RangeExpr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RangeExpr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = RangeExpr_0_0(b, l + 1);
    r = r && RangeExpr_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '..' | '...'
  private static boolean RangeExpr_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RangeExpr_0_0")) return false;
    boolean r;
    r = consumeTokenSmart(b, RANGE);
    if (!r) r = consumeTokenSmart(b, TRIPLE_DOT);
    return r;
  }

  // !']'
  private static boolean RangeExpr_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RangeExpr_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeTokenSmart(b, RBRACK);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  public static boolean UnaryExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "UnaryExpr")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = UnaryOp(b, l + 1);
    p = r;
    r = p && Expression(b, l, 7);
    exit_section_(b, l, m, UNARY_EXPR, r, p, null);
    return r || p;
  }

  // match BeforeBlockExpression '{' MatchArms '}'
  public static boolean MatchExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MatchExpression")) return false;
    if (!nextTokenIsSmart(b, MATCH)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, MATCH_EXPRESSION, null);
    r = consumeTokenSmart(b, MATCH);
    p = r; // pin = 1
    r = r && report_error_(b, BeforeBlockExpression(b, l + 1));
    r = p && report_error_(b, consumeToken(b, LBRACE)) && r;
    r = p && report_error_(b, MatchArms(b, l + 1)) && r;
    r = p && consumeToken(b, RBRACE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // select '{' SelectArms '}'
  public static boolean SelectExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SelectExpression")) return false;
    if (!nextTokenIsSmart(b, SELECT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SELECT_EXPRESSION, null);
    r = consumeTokensSmart(b, 2, SELECT, LBRACE);
    p = r; // pin = 2
    r = r && report_error_(b, SelectArms(b, l + 1));
    r = p && consumeToken(b, RBRACE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // '$if' Condition semi? Block (semi? CompileTimeElseBranch)?
  public static boolean CompileTimeIfExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CompileTimeIfExpression")) return false;
    if (!nextTokenIsSmart(b, IF_COMPILE_TIME)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, COMPILE_TIME_IF_EXPRESSION, null);
    r = consumeTokenSmart(b, IF_COMPILE_TIME);
    p = r; // pin = 1
    r = r && report_error_(b, Condition(b, l + 1));
    r = p && report_error_(b, CompileTimeIfExpression_2(b, l + 1)) && r;
    r = p && report_error_(b, Block(b, l + 1)) && r;
    r = p && CompileTimeIfExpression_4(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // semi?
  private static boolean CompileTimeIfExpression_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CompileTimeIfExpression_2")) return false;
    semi(b, l + 1);
    return true;
  }

  // (semi? CompileTimeElseBranch)?
  private static boolean CompileTimeIfExpression_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CompileTimeIfExpression_4")) return false;
    CompileTimeIfExpression_4_0(b, l + 1);
    return true;
  }

  // semi? CompileTimeElseBranch
  private static boolean CompileTimeIfExpression_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CompileTimeIfExpression_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = CompileTimeIfExpression_4_0_0(b, l + 1);
    r = r && CompileTimeElseBranch(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // semi?
  private static boolean CompileTimeIfExpression_4_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CompileTimeIfExpression_4_0_0")) return false;
    semi(b, l + 1);
    return true;
  }

  // is Type
  private static boolean IsExpression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IsExpression_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, IS);
    r = r && Type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '!is' Type
  private static boolean NotIsExpression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "NotIsExpression_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, NOT_IS);
    r = r && Type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // as Type
  private static boolean AsExpression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AsExpression_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, AS);
    r = r && Type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // DotPrimaryExpr RightHandExprs
  public static boolean DotExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "DotExpression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, DOT_EXPRESSION, "<dot expression>");
    r = DotPrimaryExpr(b, l + 1);
    r = r && RightHandExprs(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // int
  //   | float
  //   | hex
  //   | oct
  //   | bin
  //   | true
  //   | false
  //   | StringLiteral
  //   | char
  //   | none
  //   | nil
  public static boolean Literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Literal")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, LITERAL, "<literal>");
    r = consumeTokenSmart(b, INT);
    if (!r) r = consumeTokenSmart(b, FLOAT);
    if (!r) r = consumeTokenSmart(b, HEX);
    if (!r) r = consumeTokenSmart(b, OCT);
    if (!r) r = consumeTokenSmart(b, BIN);
    if (!r) r = consumeTokenSmart(b, TRUE);
    if (!r) r = consumeTokenSmart(b, FALSE);
    if (!r) r = StringLiteral(b, l + 1);
    if (!r) r = consumeTokenSmart(b, CHAR);
    if (!r) r = consumeTokenSmart(b, NONE);
    if (!r) r = consumeTokenSmart(b, NIL);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // fn CaptureList? GenericParameters? Signature Block
  public static boolean FunctionLit(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FunctionLit")) return false;
    if (!nextTokenIsSmart(b, FN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_LIT, null);
    r = consumeTokenSmart(b, FN);
    p = r; // pin = 1
    r = r && report_error_(b, FunctionLit_1(b, l + 1));
    r = p && report_error_(b, FunctionLit_2(b, l + 1)) && r;
    r = p && report_error_(b, Signature(b, l + 1)) && r;
    r = p && Block(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // CaptureList?
  private static boolean FunctionLit_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FunctionLit_1")) return false;
    CaptureList(b, l + 1);
    return true;
  }

  // GenericParameters?
  private static boolean FunctionLit_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FunctionLit_2")) return false;
    GenericParameters(b, l + 1);
    return true;
  }

  // '.' identifier
  public static boolean EnumFetch(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumFetch")) return false;
    if (!nextTokenIsSmart(b, DOT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokensSmart(b, 2, DOT, IDENTIFIER);
    exit_section_(b, m, ENUM_FETCH, r);
    return r;
  }

  public static boolean MutExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MutExpression")) return false;
    if (!nextTokenIsSmart(b, MUT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, MUT);
    p = r;
    r = p && Expression(b, l, 19);
    exit_section_(b, l, m, MUT_EXPRESSION, r, p, null);
    return r || p;
  }

  public static boolean SharedExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SharedExpression")) return false;
    if (!nextTokenIsSmart(b, SHARED)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, SHARED);
    p = r;
    r = p && Expression(b, l, 20);
    exit_section_(b, l, m, SHARED_EXPRESSION, r, p, null);
    return r || p;
  }

  public static boolean GoExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GoExpression")) return false;
    if (!nextTokenIsSmart(b, GO)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, GO);
    p = r;
    r = p && Expression(b, l, 21);
    exit_section_(b, l, m, GO_EXPRESSION, r, p, null);
    return r || p;
  }

  public static boolean SpawnExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SpawnExpression")) return false;
    if (!nextTokenIsSmart(b, SPAWN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, SPAWN);
    p = r;
    r = p && Expression(b, l, 22);
    exit_section_(b, l, m, SPAWN_EXPRESSION, r, p, null);
    return r || p;
  }

  // LockParts+ Block
  public static boolean LockExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "LockExpression")) return false;
    if (!nextTokenIsSmart(b, LOCK, RLOCK)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, LOCK_EXPRESSION, "<lock expression>");
    r = LockExpression_0(b, l + 1);
    p = r; // pin = 1
    r = r && Block(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // LockParts+
  private static boolean LockExpression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "LockExpression_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = LockParts(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!LockParts(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "LockExpression_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // '++' | '--'
  private static boolean IncDecExpression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IncDecExpression_0")) return false;
    boolean r;
    r = consumeTokenSmart(b, PLUS_PLUS);
    if (!r) r = consumeTokenSmart(b, MINUS_MINUS);
    return r;
  }

  // '...' Expression
  public static boolean UnpackingExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "UnpackingExpression")) return false;
    if (!nextTokenIsSmart(b, TRIPLE_DOT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, UNPACKING_EXPRESSION, null);
    r = consumeTokenSmart(b, TRIPLE_DOT);
    p = r; // pin = 1
    r = r && Expression(b, l + 1, -1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  public static boolean TypeCastExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeCastExpression")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = TypeCastExpression_0(b, l + 1);
    p = r;
    r = p && Expression(b, l, 26);
    r = p && report_error_(b, consumeToken(b, RPAREN)) && r;
    exit_section_(b, l, m, TYPE_CAST_EXPRESSION, r, p, null);
    return r || p;
  }

  // Type '('
  private static boolean TypeCastExpression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TypeCastExpression_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = Type(b, l + 1);
    r = r && consumeToken(b, LPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // '(' <<enterMode "PAR">> Expression <<exitModeSafe "PAR">>')'
  public static boolean ParenthesesExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParenthesesExpr")) return false;
    if (!nextTokenIsSmart(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PARENTHESES_EXPR, null);
    r = consumeTokenSmart(b, LPAREN);
    p = r; // pin = 1
    r = r && report_error_(b, enterMode(b, l + 1, "PAR"));
    r = p && report_error_(b, Expression(b, l + 1, -1)) && r;
    r = p && report_error_(b, exitModeSafe(b, l + 1, "PAR")) && r;
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  static final Parser Expression_parser_ = (b, l) -> Expression(b, l + 1, -1);
}
