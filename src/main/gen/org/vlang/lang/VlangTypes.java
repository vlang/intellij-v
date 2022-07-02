// This is a generated file. Not intended for manual editing.
package org.vlang.lang;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.vlang.lang.psi.VlangCompositeElementType;
import org.vlang.lang.psi.VlangTokenType;
import org.vlang.lang.psi.impl.*;
import org.vlang.lang.stubs.VlangElementTypeFactory;

public interface VlangTypes {

  IElementType ADD_EXPR = new VlangCompositeElementType("ADD_EXPR");
  IElementType AND_EXPR = new VlangCompositeElementType("AND_EXPR");
  IElementType ARGUMENT_LIST = new VlangCompositeElementType("ARGUMENT_LIST");
  IElementType ASSIGNMENT_STATEMENT = new VlangCompositeElementType("ASSIGNMENT_STATEMENT");
  IElementType ASSIGN_OP = new VlangCompositeElementType("ASSIGN_OP");
  IElementType BLOCK = new VlangCompositeElementType("BLOCK");
  IElementType CALL_EXPR = new VlangCompositeElementType("CALL_EXPR");
  IElementType CONDITIONAL_EXPR = new VlangCompositeElementType("CONDITIONAL_EXPR");
  IElementType ELSE_STATEMENT = new VlangCompositeElementType("ELSE_STATEMENT");
  IElementType EXPRESSION = new VlangCompositeElementType("EXPRESSION");
  IElementType FUNCTION_DECLARATION = VlangElementTypeFactory.stubFactory("FUNCTION_DECLARATION");
  IElementType IF_STATEMENT = new VlangCompositeElementType("IF_STATEMENT");
  IElementType IMPORT_ALIAS = new VlangCompositeElementType("IMPORT_ALIAS");
  IElementType IMPORT_DECLARATION = new VlangCompositeElementType("IMPORT_DECLARATION");
  IElementType IMPORT_LIST = new VlangCompositeElementType("IMPORT_LIST");
  IElementType IMPORT_SPEC = new VlangCompositeElementType("IMPORT_SPEC");
  IElementType INC_DEC_STATEMENT = new VlangCompositeElementType("INC_DEC_STATEMENT");
  IElementType LEFT_HAND_EXPR_LIST = new VlangCompositeElementType("LEFT_HAND_EXPR_LIST");
  IElementType LITERAL = new VlangCompositeElementType("LITERAL");
  IElementType METHOD_DECLARATION = VlangElementTypeFactory.stubFactory("METHOD_DECLARATION");
  IElementType MUL_EXPR = new VlangCompositeElementType("MUL_EXPR");
  IElementType OR_EXPR = new VlangCompositeElementType("OR_EXPR");
  IElementType PACKAGE_CLAUSE = VlangElementTypeFactory.stubFactory("PACKAGE_CLAUSE");
  IElementType PARAMETERS = new VlangCompositeElementType("PARAMETERS");
  IElementType PARAMETER_DECLARATION = new VlangCompositeElementType("PARAMETER_DECLARATION");
  IElementType PARAM_DEFINITION = new VlangCompositeElementType("PARAM_DEFINITION");
  IElementType PARENTHESES_EXPR = new VlangCompositeElementType("PARENTHESES_EXPR");
  IElementType PAR_TYPE = new VlangCompositeElementType("PAR_TYPE");
  IElementType RECEIVER = new VlangCompositeElementType("RECEIVER");
  IElementType REFERENCE_EXPRESSION = new VlangCompositeElementType("REFERENCE_EXPRESSION");
  IElementType RESULT = new VlangCompositeElementType("RESULT");
  IElementType RETURN_STATEMENT = new VlangCompositeElementType("RETURN_STATEMENT");
  IElementType SELECTIVE_IMPORT_LIST = new VlangCompositeElementType("SELECTIVE_IMPORT_LIST");
  IElementType SIGNATURE = new VlangCompositeElementType("SIGNATURE");
  IElementType SIMPLE_STATEMENT = new VlangCompositeElementType("SIMPLE_STATEMENT");
  IElementType STATEMENT = new VlangCompositeElementType("STATEMENT");
  IElementType STRING_LITERAL = new VlangCompositeElementType("STRING_LITERAL");
  IElementType SYMBOL_VISIBILITY = new VlangCompositeElementType("SYMBOL_VISIBILITY");
  IElementType TYPE = new VlangCompositeElementType("TYPE");
  IElementType TYPE_LIST = new VlangCompositeElementType("TYPE_LIST");
  IElementType TYPE_REFERENCE_EXPRESSION = new VlangCompositeElementType("TYPE_REFERENCE_EXPRESSION");
  IElementType UNARY_EXPR = new VlangCompositeElementType("UNARY_EXPR");

  IElementType AS = new VlangTokenType("as");
  IElementType ASSIGN = new VlangTokenType("=");
  IElementType BIT_AND = new VlangTokenType("&");
  IElementType BIT_AND_ASSIGN = new VlangTokenType("&=");
  IElementType BIT_CLEAR = new VlangTokenType("&^");
  IElementType BIT_CLEAR_ASSIGN = new VlangTokenType("&^=");
  IElementType BIT_OR = new VlangTokenType("|");
  IElementType BIT_OR_ASSIGN = new VlangTokenType("|=");
  IElementType BIT_XOR = new VlangTokenType("^");
  IElementType BIT_XOR_ASSIGN = new VlangTokenType("^=");
  IElementType BREAK = new VlangTokenType("break");
  IElementType CASE = new VlangTokenType("case");
  IElementType CHAN = new VlangTokenType("chan");
  IElementType CHAR = new VlangTokenType("char");
  IElementType COLON = new VlangTokenType(":");
  IElementType COMMA = new VlangTokenType(",");
  IElementType COND_AND = new VlangTokenType("&&");
  IElementType COND_OR = new VlangTokenType("||");
  IElementType CONST = new VlangTokenType("const");
  IElementType CONTINUE = new VlangTokenType("continue");
  IElementType DECIMALI = new VlangTokenType("decimali");
  IElementType DEFAULT = new VlangTokenType("default");
  IElementType DEFER = new VlangTokenType("defer");
  IElementType DOT = new VlangTokenType(".");
  IElementType ELSE = new VlangTokenType("else");
  IElementType EQ = new VlangTokenType("==");
  IElementType FALLTHROUGH = new VlangTokenType("fallthrough");
  IElementType FLOAT = new VlangTokenType("float");
  IElementType FLOATI = new VlangTokenType("floati");
  IElementType FOR = new VlangTokenType("for");
  IElementType FUNC = new VlangTokenType("func");
  IElementType GO = new VlangTokenType("go");
  IElementType GOTO = new VlangTokenType("goto");
  IElementType GREATER = new VlangTokenType(">");
  IElementType GREATER_OR_EQUAL = new VlangTokenType(">=");
  IElementType HEX = new VlangTokenType("hex");
  IElementType IDENTIFIER = new VlangTokenType("identifier");
  IElementType IF = new VlangTokenType("if");
  IElementType IMPORT = new VlangTokenType("import");
  IElementType INT = new VlangTokenType("int");
  IElementType INTERFACE = new VlangTokenType("interface");
  IElementType LBRACE = new VlangTokenType("{");
  IElementType LBRACK = new VlangTokenType("[");
  IElementType LESS = new VlangTokenType("<");
  IElementType LESS_OR_EQUAL = new VlangTokenType("<=");
  IElementType LPAREN = new VlangTokenType("(");
  IElementType MAP = new VlangTokenType("map");
  IElementType MINUS = new VlangTokenType("-");
  IElementType MINUS_ASSIGN = new VlangTokenType("-=");
  IElementType MINUS_MINUS = new VlangTokenType("--");
  IElementType MUL = new VlangTokenType("*");
  IElementType MUL_ASSIGN = new VlangTokenType("*=");
  IElementType NOT = new VlangTokenType("!");
  IElementType NOT_EQ = new VlangTokenType("!=");
  IElementType OCT = new VlangTokenType("oct");
  IElementType PACKAGE = new VlangTokenType("package");
  IElementType PLUS = new VlangTokenType("+");
  IElementType PLUS_ASSIGN = new VlangTokenType("+=");
  IElementType PLUS_PLUS = new VlangTokenType("++");
  IElementType PUB = new VlangTokenType("pub");
  IElementType QUOTIENT = new VlangTokenType("/");
  IElementType QUOTIENT_ASSIGN = new VlangTokenType("/=");
  IElementType RAW_STRING = new VlangTokenType("raw_string");
  IElementType RBRACE = new VlangTokenType("}");
  IElementType RBRACK = new VlangTokenType("]");
  IElementType REMAINDER = new VlangTokenType("%");
  IElementType REMAINDER_ASSIGN = new VlangTokenType("%=");
  IElementType RETURN = new VlangTokenType("return");
  IElementType RPAREN = new VlangTokenType(")");
  IElementType SELECT = new VlangTokenType("select");
  IElementType SEMICOLON = new VlangTokenType(";");
  IElementType SEMICOLON_SYNTHETIC = new VlangTokenType("<NL>");
  IElementType SEND_CHANNEL = new VlangTokenType("<-");
  IElementType SHIFT_LEFT = new VlangTokenType("<<");
  IElementType SHIFT_LEFT_ASSIGN = new VlangTokenType("<<=");
  IElementType SHIFT_RIGHT = new VlangTokenType(">>");
  IElementType SHIFT_RIGHT_ASSIGN = new VlangTokenType(">>=");
  IElementType STRING = new VlangTokenType("string");
  IElementType STRUCT = new VlangTokenType("struct");
  IElementType SWITCH = new VlangTokenType("switch");
  IElementType TRIPLE_DOT = new VlangTokenType("...");
  IElementType TYPE_ = new VlangTokenType("type");
  IElementType VAR = new VlangTokenType("var");
  IElementType VAR_ASSIGN = new VlangTokenType(":=");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ADD_EXPR) {
        return new VlangAddExprImpl(node);
      }
      else if (type == AND_EXPR) {
        return new VlangAndExprImpl(node);
      }
      else if (type == ARGUMENT_LIST) {
        return new VlangArgumentListImpl(node);
      }
      else if (type == ASSIGNMENT_STATEMENT) {
        return new VlangAssignmentStatementImpl(node);
      }
      else if (type == ASSIGN_OP) {
        return new VlangAssignOpImpl(node);
      }
      else if (type == BLOCK) {
        return new VlangBlockImpl(node);
      }
      else if (type == CALL_EXPR) {
        return new VlangCallExprImpl(node);
      }
      else if (type == CONDITIONAL_EXPR) {
        return new VlangConditionalExprImpl(node);
      }
      else if (type == ELSE_STATEMENT) {
        return new VlangElseStatementImpl(node);
      }
      else if (type == FUNCTION_DECLARATION) {
        return new VlangFunctionDeclarationImpl(node);
      }
      else if (type == IF_STATEMENT) {
        return new VlangIfStatementImpl(node);
      }
      else if (type == IMPORT_ALIAS) {
        return new VlangImportAliasImpl(node);
      }
      else if (type == IMPORT_DECLARATION) {
        return new VlangImportDeclarationImpl(node);
      }
      else if (type == IMPORT_LIST) {
        return new VlangImportListImpl(node);
      }
      else if (type == IMPORT_SPEC) {
        return new VlangImportSpecImpl(node);
      }
      else if (type == INC_DEC_STATEMENT) {
        return new VlangIncDecStatementImpl(node);
      }
      else if (type == LEFT_HAND_EXPR_LIST) {
        return new VlangLeftHandExprListImpl(node);
      }
      else if (type == LITERAL) {
        return new VlangLiteralImpl(node);
      }
      else if (type == METHOD_DECLARATION) {
        return new VlangMethodDeclarationImpl(node);
      }
      else if (type == MUL_EXPR) {
        return new VlangMulExprImpl(node);
      }
      else if (type == OR_EXPR) {
        return new VlangOrExprImpl(node);
      }
      else if (type == PACKAGE_CLAUSE) {
        return new VlangPackageClauseImpl(node);
      }
      else if (type == PARAMETERS) {
        return new VlangParametersImpl(node);
      }
      else if (type == PARAMETER_DECLARATION) {
        return new VlangParameterDeclarationImpl(node);
      }
      else if (type == PARAM_DEFINITION) {
        return new VlangParamDefinitionImpl(node);
      }
      else if (type == PARENTHESES_EXPR) {
        return new VlangParenthesesExprImpl(node);
      }
      else if (type == PAR_TYPE) {
        return new VlangParTypeImpl(node);
      }
      else if (type == RECEIVER) {
        return new VlangReceiverImpl(node);
      }
      else if (type == REFERENCE_EXPRESSION) {
        return new VlangReferenceExpressionImpl(node);
      }
      else if (type == RESULT) {
        return new VlangResultImpl(node);
      }
      else if (type == RETURN_STATEMENT) {
        return new VlangReturnStatementImpl(node);
      }
      else if (type == SELECTIVE_IMPORT_LIST) {
        return new VlangSelectiveImportListImpl(node);
      }
      else if (type == SIGNATURE) {
        return new VlangSignatureImpl(node);
      }
      else if (type == SIMPLE_STATEMENT) {
        return new VlangSimpleStatementImpl(node);
      }
      else if (type == STRING_LITERAL) {
        return new VlangStringLiteralImpl(node);
      }
      else if (type == SYMBOL_VISIBILITY) {
        return new VlangSymbolVisibilityImpl(node);
      }
      else if (type == TYPE) {
        return new VlangTypeImpl(node);
      }
      else if (type == TYPE_LIST) {
        return new VlangTypeListImpl(node);
      }
      else if (type == TYPE_REFERENCE_EXPRESSION) {
        return new VlangTypeReferenceExpressionImpl(node);
      }
      else if (type == UNARY_EXPR) {
        return new VlangUnaryExprImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
