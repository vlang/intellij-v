// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

public class VlangVisitor extends PsiElementVisitor {

  public void visitAddExpr(@NotNull VlangAddExpr o) {
    visitBinaryExpr(o);
  }

  public void visitAndExpr(@NotNull VlangAndExpr o) {
    visitBinaryExpr(o);
  }

  public void visitArgumentList(@NotNull VlangArgumentList o) {
    visitCompositeElement(o);
  }

  public void visitAssignmentStatement(@NotNull VlangAssignmentStatement o) {
    visitStatement(o);
  }

  public void visitBinaryExpr(@NotNull VlangBinaryExpr o) {
    visitExpression(o);
  }

  public void visitBlock(@NotNull VlangBlock o) {
    visitCompositeElement(o);
  }

  public void visitCallExpr(@NotNull VlangCallExpr o) {
    visitExpression(o);
  }

  public void visitConditionalExpr(@NotNull VlangConditionalExpr o) {
    visitBinaryExpr(o);
  }

  public void visitElseStatement(@NotNull VlangElseStatement o) {
    visitStatement(o);
  }

  public void visitExpression(@NotNull VlangExpression o) {
    visitCompositeElement(o);
  }

  public void visitForClause(@NotNull VlangForClause o) {
    visitCompositeElement(o);
  }

  public void visitForStatement(@NotNull VlangForStatement o) {
    visitStatement(o);
  }

  public void visitFunctionDeclaration(@NotNull VlangFunctionDeclaration o) {
    visitFunctionOrMethodDeclaration(o);
  }

  public void visitIfStatement(@NotNull VlangIfStatement o) {
    visitStatement(o);
  }

  public void visitImportAlias(@NotNull VlangImportAlias o) {
    visitCompositeElement(o);
  }

  public void visitImportDeclaration(@NotNull VlangImportDeclaration o) {
    visitCompositeElement(o);
  }

  public void visitImportList(@NotNull VlangImportList o) {
    visitCompositeElement(o);
  }

  public void visitImportSpec(@NotNull VlangImportSpec o) {
    visitNamedElement(o);
  }

  public void visitIncDecStatement(@NotNull VlangIncDecStatement o) {
    visitStatement(o);
  }

  public void visitLeftHandExprList(@NotNull VlangLeftHandExprList o) {
    visitCompositeElement(o);
  }

  public void visitLiteral(@NotNull VlangLiteral o) {
    visitExpression(o);
  }

  public void visitMethodDeclaration(@NotNull VlangMethodDeclaration o) {
    visitFunctionOrMethodDeclaration(o);
  }

  public void visitMulExpr(@NotNull VlangMulExpr o) {
    visitBinaryExpr(o);
  }

  public void visitOrExpr(@NotNull VlangOrExpr o) {
    visitBinaryExpr(o);
  }

  public void visitPackageClause(@NotNull VlangPackageClause o) {
    visitCompositeElement(o);
  }

  public void visitParType(@NotNull VlangParType o) {
    visitType(o);
  }

  public void visitParamDefinition(@NotNull VlangParamDefinition o) {
    visitNamedElement(o);
  }

  public void visitParameterDeclaration(@NotNull VlangParameterDeclaration o) {
    visitCompositeElement(o);
  }

  public void visitParameters(@NotNull VlangParameters o) {
    visitCompositeElement(o);
  }

  public void visitParenthesesExpr(@NotNull VlangParenthesesExpr o) {
    visitExpression(o);
  }

  public void visitRangeClause(@NotNull VlangRangeClause o) {
    visitShortVarDeclaration(o);
  }

  public void visitReceiver(@NotNull VlangReceiver o) {
    visitNamedElement(o);
  }

  public void visitReferenceExpression(@NotNull VlangReferenceExpression o) {
    visitExpression(o);
    // visitReferenceExpressionBase(o);
  }

  public void visitResult(@NotNull VlangResult o) {
    visitCompositeElement(o);
  }

  public void visitReturnStatement(@NotNull VlangReturnStatement o) {
    visitStatement(o);
  }

  public void visitSelectiveImportList(@NotNull VlangSelectiveImportList o) {
    visitCompositeElement(o);
  }

  public void visitShortVarDeclaration(@NotNull VlangShortVarDeclaration o) {
    visitCompositeElement(o);
  }

  public void visitSignature(@NotNull VlangSignature o) {
    visitCompositeElement(o);
  }

  public void visitSimpleStatement(@NotNull VlangSimpleStatement o) {
    visitStatement(o);
  }

  public void visitStatement(@NotNull VlangStatement o) {
    visitCompositeElement(o);
  }

  public void visitStringLiteral(@NotNull VlangStringLiteral o) {
    visitExpression(o);
  }

  public void visitSymbolMutability(@NotNull VlangSymbolMutability o) {
    visitCompositeElement(o);
  }

  public void visitSymbolVisibility(@NotNull VlangSymbolVisibility o) {
    visitCompositeElement(o);
  }

  public void visitType(@NotNull VlangType o) {
    visitCompositeElement(o);
  }

  public void visitTypeList(@NotNull VlangTypeList o) {
    visitType(o);
  }

  public void visitTypeReferenceExpression(@NotNull VlangTypeReferenceExpression o) {
    visitReferenceExpressionBase(o);
  }

  public void visitUnaryExpr(@NotNull VlangUnaryExpr o) {
    visitExpression(o);
  }

  public void visitVarDefinition(@NotNull VlangVarDefinition o) {
    visitNamedElement(o);
  }

  public void visitAssignOp(@NotNull VlangAssignOp o) {
    visitCompositeElement(o);
  }

  public void visitFunctionOrMethodDeclaration(@NotNull VlangFunctionOrMethodDeclaration o) {
    visitCompositeElement(o);
  }

  public void visitNamedElement(@NotNull VlangNamedElement o) {
    visitCompositeElement(o);
  }

  public void visitReferenceExpressionBase(@NotNull VlangReferenceExpressionBase o) {
    visitCompositeElement(o);
  }

  public void visitCompositeElement(@NotNull VlangCompositeElement o) {
    visitElement(o);
  }

}
