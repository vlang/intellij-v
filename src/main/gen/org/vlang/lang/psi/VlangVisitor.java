// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;

public class VlangVisitor extends PsiElementVisitor {

  public void visitAddExpr(@NotNull VlangAddExpr o) {
    visitBinaryExpr(o);
  }

  public void visitAndExpr(@NotNull VlangAndExpr o) {
    visitBinaryExpr(o);
  }

  public void visitAnonymousFieldDefinition(@NotNull VlangAnonymousFieldDefinition o) {
    visitCompositeElement(o);
  }

  public void visitAnonymousInterfaceDefinition(@NotNull VlangAnonymousInterfaceDefinition o) {
    visitCompositeElement(o);
  }

  public void visitArgumentList(@NotNull VlangArgumentList o) {
    visitCompositeElement(o);
  }

  public void visitArrayCreation(@NotNull VlangArrayCreation o) {
    visitExpression(o);
  }

  public void visitArrayCreationList(@NotNull VlangArrayCreationList o) {
    visitCompositeElement(o);
  }

  public void visitArrayOrSliceType(@NotNull VlangArrayOrSliceType o) {
    visitTypeDecl(o);
  }

  public void visitAsExpression(@NotNull VlangAsExpression o) {
    visitExpression(o);
  }

  public void visitAssertStatement(@NotNull VlangAssertStatement o) {
    visitStatement(o);
  }

  public void visitAssignmentStatement(@NotNull VlangAssignmentStatement o) {
    visitStatement(o);
  }

  public void visitAttribute(@NotNull VlangAttribute o) {
    visitCompositeElement(o);
  }

  public void visitAttributeExpression(@NotNull VlangAttributeExpression o) {
    visitCompositeElement(o);
  }

  public void visitAttributes(@NotNull VlangAttributes o) {
    visitCompositeElement(o);
  }

  public void visitBinaryExpr(@NotNull VlangBinaryExpr o) {
    visitExpression(o);
  }

  public void visitBlock(@NotNull VlangBlock o) {
    visitCompositeElement(o);
  }

  public void visitBreakStatement(@NotNull VlangBreakStatement o) {
    visitStatement(o);
  }

  public void visitCFlagStatement(@NotNull VlangCFlagStatement o) {
    visitStatement(o);
  }

  public void visitCIncludeStatement(@NotNull VlangCIncludeStatement o) {
    visitStatement(o);
  }

  public void visitCallExpr(@NotNull VlangCallExpr o) {
    visitExpression(o);
  }

  public void visitCompileElseStatement(@NotNull VlangCompileElseStatement o) {
    visitStatement(o);
  }

  public void visitCompileTimeIfExpression(@NotNull VlangCompileTimeIfExpression o) {
    visitExpression(o);
  }

  public void visitCompileTimeIfStatement(@NotNull VlangCompileTimeIfStatement o) {
    visitStatement(o);
  }

  public void visitConditionalExpr(@NotNull VlangConditionalExpr o) {
    visitBinaryExpr(o);
  }

  public void visitConstDeclaration(@NotNull VlangConstDeclaration o) {
    visitCompositeElement(o);
  }

  public void visitConstDefinition(@NotNull VlangConstDefinition o) {
    visitNamedElement(o);
  }

  public void visitConstSpec(@NotNull VlangConstSpec o) {
    visitCompositeElement(o);
  }

  public void visitContinueStatement(@NotNull VlangContinueStatement o) {
    visitStatement(o);
  }

  public void visitDefaultFieldValue(@NotNull VlangDefaultFieldValue o) {
    visitCompositeElement(o);
  }

  public void visitDeferStatement(@NotNull VlangDeferStatement o) {
    visitStatement(o);
  }

  public void visitDotExpression(@NotNull VlangDotExpression o) {
    visitExpression(o);
  }

  public void visitElseStatement(@NotNull VlangElseStatement o) {
    visitStatement(o);
  }

  public void visitEnumDeclaration(@NotNull VlangEnumDeclaration o) {
    visitCompositeElement(o);
  }

  public void visitEnumFetch(@NotNull VlangEnumFetch o) {
    visitExpression(o);
  }

  public void visitEnumFieldDeclaration(@NotNull VlangEnumFieldDeclaration o) {
    visitCompositeElement(o);
  }

  public void visitEnumFields(@NotNull VlangEnumFields o) {
    visitCompositeElement(o);
  }

  public void visitErrorPropagation(@NotNull VlangErrorPropagation o) {
    visitCompositeElement(o);
  }

  public void visitExpression(@NotNull VlangExpression o) {
    visitCompositeElement(o);
  }

  public void visitFieldDeclaration(@NotNull VlangFieldDeclaration o) {
    visitCompositeElement(o);
  }

  public void visitFieldInitialization(@NotNull VlangFieldInitialization o) {
    visitCompositeElement(o);
  }

  public void visitFieldInitializationKeyValueList(@NotNull VlangFieldInitializationKeyValueList o) {
    visitCompositeElement(o);
  }

  public void visitFieldLookup(@NotNull VlangFieldLookup o) {
    visitCompositeElement(o);
  }

  public void visitFieldName(@NotNull VlangFieldName o) {
    visitReferenceExpressionBase(o);
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

  public void visitFunctionLit(@NotNull VlangFunctionLit o) {
    visitExpression(o);
  }

  public void visitFunctionType(@NotNull VlangFunctionType o) {
    visitTypeDecl(o);
  }

  public void visitGlobalVariableDeclaration(@NotNull VlangGlobalVariableDeclaration o) {
    visitCompositeElement(o);
  }

  public void visitGoStatement(@NotNull VlangGoStatement o) {
    visitStatement(o);
  }

  public void visitIfAttribute(@NotNull VlangIfAttribute o) {
    visitCompositeElement(o);
  }

  public void visitIfExpression(@NotNull VlangIfExpression o) {
    visitExpression(o);
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

  public void visitInExpression(@NotNull VlangInExpression o) {
    visitExpression(o);
  }

  public void visitIncDecStatement(@NotNull VlangIncDecStatement o) {
    visitStatement(o);
  }

  public void visitIndexOrSliceExpr(@NotNull VlangIndexOrSliceExpr o) {
    visitExpression(o);
  }

  public void visitInterfaceDeclaration(@NotNull VlangInterfaceDeclaration o) {
    visitCompositeElement(o);
  }

  public void visitInterfaceFieldDeclaration(@NotNull VlangInterfaceFieldDeclaration o) {
    visitCompositeElement(o);
  }

  public void visitInterfaceMethodDeclaration(@NotNull VlangInterfaceMethodDeclaration o) {
    visitCompositeElement(o);
  }

  public void visitInterfaceType(@NotNull VlangInterfaceType o) {
    visitTypeDecl(o);
  }

  public void visitIsExpression(@NotNull VlangIsExpression o) {
    visitExpression(o);
  }

  public void visitLabelRef(@NotNull VlangLabelRef o) {
    visitCompositeElement(o);
  }

  public void visitLanguageInjectionStatement(@NotNull VlangLanguageInjectionStatement o) {
    visitStatement(o);
  }

  public void visitLeftHandExprList(@NotNull VlangLeftHandExprList o) {
    visitCompositeElement(o);
  }

  public void visitLiteral(@NotNull VlangLiteral o) {
    visitExpression(o);
  }

  public void visitLiteralTypeExpr(@NotNull VlangLiteralTypeExpr o) {
    visitExpression(o);
  }

  public void visitMatchArm(@NotNull VlangMatchArm o) {
    visitCompositeElement(o);
  }

  public void visitMatchArms(@NotNull VlangMatchArms o) {
    visitCompositeElement(o);
  }

  public void visitMatchElseArmClause(@NotNull VlangMatchElseArmClause o) {
    visitCompositeElement(o);
  }

  public void visitMatchExpression(@NotNull VlangMatchExpression o) {
    visitExpression(o);
  }

  public void visitMemberModifier(@NotNull VlangMemberModifier o) {
    visitCompositeElement(o);
  }

  public void visitMemberModifiers(@NotNull VlangMemberModifiers o) {
    visitCompositeElement(o);
  }

  public void visitMethodCall(@NotNull VlangMethodCall o) {
    visitCompositeElement(o);
  }

  public void visitMethodDeclaration(@NotNull VlangMethodDeclaration o) {
    visitFunctionOrMethodDeclaration(o);
  }

  public void visitModuleClause(@NotNull VlangModuleClause o) {
    visitCompositeElement(o);
  }

  public void visitMulExpr(@NotNull VlangMulExpr o) {
    visitBinaryExpr(o);
  }

  public void visitMutExpression(@NotNull VlangMutExpression o) {
    visitExpression(o);
  }

  public void visitNotInExpression(@NotNull VlangNotInExpression o) {
    visitExpression(o);
  }

  public void visitNotIsExpression(@NotNull VlangNotIsExpression o) {
    visitExpression(o);
  }

  public void visitNullableType(@NotNull VlangNullableType o) {
    visitTypeDecl(o);
  }

  public void visitOrBlockExpr(@NotNull VlangOrBlockExpr o) {
    visitBinaryExpr(o);
  }

  public void visitOrExpr(@NotNull VlangOrExpr o) {
    visitBinaryExpr(o);
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

  public void visitPlainAttribute(@NotNull VlangPlainAttribute o) {
    visitCompositeElement(o);
  }

  public void visitPointerType(@NotNull VlangPointerType o) {
    visitTypeDecl(o);
  }

  public void visitRangeClause(@NotNull VlangRangeClause o) {
    visitShortVarDeclaration(o);
  }

  public void visitRangeExpr(@NotNull VlangRangeExpr o) {
    visitExpression(o);
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

  public void visitStructDeclaration(@NotNull VlangStructDeclaration o) {
    visitCompositeElement(o);
  }

  public void visitStructInitialization(@NotNull VlangStructInitialization o) {
    visitExpression(o);
  }

  public void visitStructType(@NotNull VlangStructType o) {
    visitTypeDecl(o);
  }

  public void visitSymbolMutability(@NotNull VlangSymbolMutability o) {
    visitCompositeElement(o);
  }

  public void visitSymbolVisibility(@NotNull VlangSymbolVisibility o) {
    visitCompositeElement(o);
  }

  public void visitTag(@NotNull VlangTag o) {
    visitCompositeElement(o);
  }

  public void visitTypeDecl(@NotNull VlangTypeDecl o) {
    visitCompositeElement(o);
  }

  public void visitTypeList(@NotNull VlangTypeList o) {
    visitTypeDecl(o);
  }

  public void visitTypeReferenceExpression(@NotNull VlangTypeReferenceExpression o) {
    visitReferenceExpressionBase(o);
  }

  public void visitTypeStatement(@NotNull VlangTypeStatement o) {
    visitStatement(o);
  }

  public void visitTypeUnionList(@NotNull VlangTypeUnionList o) {
    visitCompositeElement(o);
  }

  public void visitUnaryExpr(@NotNull VlangUnaryExpr o) {
    visitExpression(o);
  }

  public void visitUnsafeExpression(@NotNull VlangUnsafeExpression o) {
    visitExpression(o);
  }

  public void visitUnsafeStatement(@NotNull VlangUnsafeStatement o) {
    visitStatement(o);
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
