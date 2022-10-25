// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiNameIdentifierOwner;
import org.jetbrains.annotations.NotNull;

public class VlangVisitor extends PsiElementVisitor {

  public void visitAddExpr(@NotNull VlangAddExpr o) {
    visitBinaryExpr(o);
  }

  public void visitAliasType(@NotNull VlangAliasType o) {
    visitType(o);
  }

  public void visitAndExpr(@NotNull VlangAndExpr o) {
    visitBinaryExpr(o);
  }

  public void visitAnonymousFieldDefinition(@NotNull VlangAnonymousFieldDefinition o) {
    visitTypeOwner(o);
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
    visitType(o);
  }

  public void visitAsExpression(@NotNull VlangAsExpression o) {
    visitExpression(o);
  }

  public void visitAsmBlock(@NotNull VlangAsmBlock o) {
    visitCompositeElement(o);
  }

  public void visitAsmBlockStatement(@NotNull VlangAsmBlockStatement o) {
    visitStatement(o);
  }

  public void visitAssertStatement(@NotNull VlangAssertStatement o) {
    visitStatement(o);
  }

  public void visitAssignOp(@NotNull VlangAssignOp o) {
    visitCompositeElement(o);
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
    // visitLabelRefOwnerElement(o);
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

  public void visitCapture(@NotNull VlangCapture o) {
    visitCompositeElement(o);
  }

  public void visitCaptureList(@NotNull VlangCaptureList o) {
    visitCompositeElement(o);
  }

  public void visitChannelType(@NotNull VlangChannelType o) {
    visitType(o);
  }

  public void visitCompileElseStatement(@NotNull VlangCompileElseStatement o) {
    visitStatement(o);
  }

  public void visitCompileTimeForStatement(@NotNull VlangCompileTimeForStatement o) {
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

  public void visitConstexprIdentifierExpression(@NotNull VlangConstexprIdentifierExpression o) {
    visitExpression(o);
  }

  public void visitContinueStatement(@NotNull VlangContinueStatement o) {
    visitStatement(o);
    // visitLabelRefOwnerElement(o);
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

  public void visitElement(@NotNull VlangElement o) {
    visitCompositeElement(o);
  }

  public void visitElseStatement(@NotNull VlangElseStatement o) {
    visitStatement(o);
  }

  public void visitEmptySlice(@NotNull VlangEmptySlice o) {
    visitCompositeElement(o);
  }

  public void visitEnumDeclaration(@NotNull VlangEnumDeclaration o) {
    visitNamedElement(o);
  }

  public void visitEnumFetch(@NotNull VlangEnumFetch o) {
    visitExpression(o);
  }

  public void visitEnumFieldDeclaration(@NotNull VlangEnumFieldDeclaration o) {
    visitCompositeElement(o);
  }

  public void visitEnumFieldDefinition(@NotNull VlangEnumFieldDefinition o) {
    visitNamedElement(o);
  }

  public void visitEnumFields(@NotNull VlangEnumFields o) {
    visitCompositeElement(o);
  }

  public void visitEnumType(@NotNull VlangEnumType o) {
    visitType(o);
  }

  public void visitErrorPropagationExpression(@NotNull VlangErrorPropagationExpression o) {
    visitCompositeElement(o);
  }

  public void visitExpression(@NotNull VlangExpression o) {
    visitTypeOwner(o);
  }

  public void visitFieldDeclaration(@NotNull VlangFieldDeclaration o) {
    visitCompositeElement(o);
  }

  public void visitFieldDefinition(@NotNull VlangFieldDefinition o) {
    visitMutable(o);
    // visitNamedElement(o);
  }

  public void visitFieldName(@NotNull VlangFieldName o) {
    visitReferenceExpressionBase(o);
  }

  public void visitFieldsGroup(@NotNull VlangFieldsGroup o) {
    visitCompositeElement(o);
  }

  public void visitForClause(@NotNull VlangForClause o) {
    visitCompositeElement(o);
  }

  public void visitForStatement(@NotNull VlangForStatement o) {
    visitStatement(o);
  }

  public void visitForceNoErrorPropagationExpression(@NotNull VlangForceNoErrorPropagationExpression o) {
    visitCompositeElement(o);
  }

  public void visitFormatSpecifier(@NotNull VlangFormatSpecifier o) {
    visitCompositeElement(o);
  }

  public void visitFormatSpecifierExpression(@NotNull VlangFormatSpecifierExpression o) {
    visitCompositeElement(o);
  }

  public void visitFormatSpecifierLeftAlignFlag(@NotNull VlangFormatSpecifierLeftAlignFlag o) {
    visitCompositeElement(o);
  }

  public void visitFormatSpecifierLetter(@NotNull VlangFormatSpecifierLetter o) {
    visitCompositeElement(o);
  }

  public void visitFormatSpecifierRightAlignFlag(@NotNull VlangFormatSpecifierRightAlignFlag o) {
    visitCompositeElement(o);
  }

  public void visitFormatSpecifierWidthAndPrecision(@NotNull VlangFormatSpecifierWidthAndPrecision o) {
    visitCompositeElement(o);
  }

  public void visitFunctionDeclaration(@NotNull VlangFunctionDeclaration o) {
    visitSignatureOwner(o);
    // visitFunctionOrMethodDeclaration(o);
  }

  public void visitFunctionLit(@NotNull VlangFunctionLit o) {
    visitExpression(o);
    // visitSignatureOwner(o);
  }

  public void visitFunctionType(@NotNull VlangFunctionType o) {
    visitType(o);
    // visitSignatureOwner(o);
  }

  public void visitGenericArguments(@NotNull VlangGenericArguments o) {
    visitCompositeElement(o);
  }

  public void visitGenericArgumentsFirstPin(@NotNull VlangGenericArgumentsFirstPin o) {
    visitCompositeElement(o);
  }

  public void visitGlobalVariableDeclaration(@NotNull VlangGlobalVariableDeclaration o) {
    visitCompositeElement(o);
  }

  public void visitGlobalVariableDefinition(@NotNull VlangGlobalVariableDefinition o) {
    visitNamedElement(o);
  }

  public void visitGoExpression(@NotNull VlangGoExpression o) {
    visitExpression(o);
  }

  public void visitGoStatement(@NotNull VlangGoStatement o) {
    visitStatement(o);
  }

  public void visitGotoStatement(@NotNull VlangGotoStatement o) {
    visitStatement(o);
    // visitLabelRefOwnerElement(o);
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
    visitNamedElement(o);
  }

  public void visitImportAliasName(@NotNull VlangImportAliasName o) {
    visitCompositeElement(o);
  }

  public void visitImportDeclaration(@NotNull VlangImportDeclaration o) {
    visitCompositeElement(o);
  }

  public void visitImportList(@NotNull VlangImportList o) {
    visitCompositeElement(o);
  }

  public void visitImportName(@NotNull VlangImportName o) {
    visitPsiNameIdentifierOwner(o);
  }

  public void visitImportPath(@NotNull VlangImportPath o) {
    visitCompositeElement(o);
  }

  public void visitImportSpec(@NotNull VlangImportSpec o) {
    visitCompositeElement(o);
  }

  public void visitInExpression(@NotNull VlangInExpression o) {
    visitExpression(o);
  }

  public void visitIncDecExpression(@NotNull VlangIncDecExpression o) {
    visitExpression(o);
  }

  public void visitIndexOrSliceExpr(@NotNull VlangIndexOrSliceExpr o) {
    visitExpression(o);
  }

  public void visitInterfaceDeclaration(@NotNull VlangInterfaceDeclaration o) {
    visitNamedElement(o);
  }

  public void visitInterfaceMethodDeclaration(@NotNull VlangInterfaceMethodDeclaration o) {
    visitCompositeElement(o);
  }

  public void visitInterfaceMethodDefinition(@NotNull VlangInterfaceMethodDefinition o) {
    visitSignatureOwner(o);
    // visitNamedElement(o);
  }

  public void visitInterfaceType(@NotNull VlangInterfaceType o) {
    visitType(o);
    // visitFieldListOwner(o);
  }

  public void visitIsExpression(@NotNull VlangIsExpression o) {
    visitExpression(o);
  }

  public void visitJsonArgumentList(@NotNull VlangJsonArgumentList o) {
    visitArgumentList(o);
  }

  public void visitJsonCallExpr(@NotNull VlangJsonCallExpr o) {
    visitCallExpr(o);
  }

  public void visitKey(@NotNull VlangKey o) {
    visitCompositeElement(o);
  }

  public void visitKeyValue(@NotNull VlangKeyValue o) {
    visitCompositeElement(o);
  }

  public void visitKeyValues(@NotNull VlangKeyValues o) {
    visitCompositeElement(o);
  }

  public void visitLabelDefinition(@NotNull VlangLabelDefinition o) {
    visitNamedElement(o);
  }

  public void visitLabelRef(@NotNull VlangLabelRef o) {
    visitCompositeElement(o);
  }

  public void visitLabeledStatement(@NotNull VlangLabeledStatement o) {
    visitStatement(o);
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

  public void visitLiteralValueExpression(@NotNull VlangLiteralValueExpression o) {
    visitExpression(o);
  }

  public void visitLockExpression(@NotNull VlangLockExpression o) {
    visitExpression(o);
  }

  public void visitLockStatement(@NotNull VlangLockStatement o) {
    visitStatement(o);
  }

  public void visitLongStringTemplateEntry(@NotNull VlangLongStringTemplateEntry o) {
    visitCompositeElement(o);
  }

  public void visitMapInitExpr(@NotNull VlangMapInitExpr o) {
    visitExpression(o);
  }

  public void visitMapType(@NotNull VlangMapType o) {
    visitType(o);
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

  public void visitMembersGroup(@NotNull VlangMembersGroup o) {
    visitCompositeElement(o);
  }

  public void visitMethodDeclaration(@NotNull VlangMethodDeclaration o) {
    visitSignatureOwner(o);
    // visitFunctionOrMethodDeclaration(o);
  }

  public void visitMethodName(@NotNull VlangMethodName o) {
    visitCompositeElement(o);
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

  public void visitNotNullableType(@NotNull VlangNotNullableType o) {
    visitType(o);
  }

  public void visitNullableType(@NotNull VlangNullableType o) {
    visitType(o);
  }

  public void visitOrBlockExpr(@NotNull VlangOrBlockExpr o) {
    visitBinaryExpr(o);
  }

  public void visitOrExpr(@NotNull VlangOrExpr o) {
    visitBinaryExpr(o);
  }

  public void visitParamDefinition(@NotNull VlangParamDefinition o) {
    visitMutable(o);
    // visitNamedElement(o);
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
    visitType(o);
  }

  public void visitRangeClause(@NotNull VlangRangeClause o) {
    visitVarDeclaration(o);
  }

  public void visitRangeExpr(@NotNull VlangRangeExpr o) {
    visitExpression(o);
  }

  public void visitReceiver(@NotNull VlangReceiver o) {
    visitMutable(o);
    // visitNamedElement(o);
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

  public void visitSelectArm(@NotNull VlangSelectArm o) {
    visitCompositeElement(o);
  }

  public void visitSelectArms(@NotNull VlangSelectArms o) {
    visitCompositeElement(o);
  }

  public void visitSelectElseArmClause(@NotNull VlangSelectElseArmClause o) {
    visitCompositeElement(o);
  }

  public void visitSelectExpression(@NotNull VlangSelectExpression o) {
    visitExpression(o);
  }

  public void visitSelectiveImportList(@NotNull VlangSelectiveImportList o) {
    visitCompositeElement(o);
  }

  public void visitSendExpr(@NotNull VlangSendExpr o) {
    visitExpression(o);
  }

  public void visitSendStatement(@NotNull VlangSendStatement o) {
    visitStatement(o);
  }

  public void visitSharedExpression(@NotNull VlangSharedExpression o) {
    visitExpression(o);
  }

  public void visitShortStringTemplateEntry(@NotNull VlangShortStringTemplateEntry o) {
    visitCompositeElement(o);
  }

  public void visitSignature(@NotNull VlangSignature o) {
    visitCompositeElement(o);
  }

  public void visitSimpleStatement(@NotNull VlangSimpleStatement o) {
    visitStatement(o);
  }

  public void visitSqlBlock(@NotNull VlangSqlBlock o) {
    visitCompositeElement(o);
  }

  public void visitSqlBlockStatement(@NotNull VlangSqlBlockStatement o) {
    visitCompositeElement(o);
  }

  public void visitSqlCreateStatement(@NotNull VlangSqlCreateStatement o) {
    visitSqlBlockStatement(o);
  }

  public void visitSqlDeleteStatement(@NotNull VlangSqlDeleteStatement o) {
    visitSqlBlockStatement(o);
  }

  public void visitSqlDropStatement(@NotNull VlangSqlDropStatement o) {
    visitSqlBlockStatement(o);
  }

  public void visitSqlExpression(@NotNull VlangSqlExpression o) {
    visitExpression(o);
  }

  public void visitSqlFromClause(@NotNull VlangSqlFromClause o) {
    visitCompositeElement(o);
  }

  public void visitSqlInsertStatement(@NotNull VlangSqlInsertStatement o) {
    visitSqlBlockStatement(o);
  }

  public void visitSqlLimitClause(@NotNull VlangSqlLimitClause o) {
    visitCompositeElement(o);
  }

  public void visitSqlOffsetClause(@NotNull VlangSqlOffsetClause o) {
    visitCompositeElement(o);
  }

  public void visitSqlOrderByClause(@NotNull VlangSqlOrderByClause o) {
    visitCompositeElement(o);
  }

  public void visitSqlReferenceList(@NotNull VlangSqlReferenceList o) {
    visitCompositeElement(o);
  }

  public void visitSqlReferenceListItem(@NotNull VlangSqlReferenceListItem o) {
    visitCompositeElement(o);
  }

  public void visitSqlSelectCountClause(@NotNull VlangSqlSelectCountClause o) {
    visitCompositeElement(o);
  }

  public void visitSqlSelectStatement(@NotNull VlangSqlSelectStatement o) {
    visitSqlBlockStatement(o);
  }

  public void visitSqlStatement(@NotNull VlangSqlStatement o) {
    visitSqlBlockStatement(o);
  }

  public void visitSqlTableName(@NotNull VlangSqlTableName o) {
    visitCompositeElement(o);
  }

  public void visitSqlUpdateItem(@NotNull VlangSqlUpdateItem o) {
    visitCompositeElement(o);
  }

  public void visitSqlUpdateList(@NotNull VlangSqlUpdateList o) {
    visitCompositeElement(o);
  }

  public void visitSqlUpdateStatement(@NotNull VlangSqlUpdateStatement o) {
    visitSqlBlockStatement(o);
  }

  public void visitSqlWhereClause(@NotNull VlangSqlWhereClause o) {
    visitCompositeElement(o);
  }

  public void visitStatement(@NotNull VlangStatement o) {
    visitCompositeElement(o);
  }

  public void visitStringLiteral(@NotNull VlangStringLiteral o) {
    visitExpression(o);
    // visitPsiLanguageInjectionHost(o);
  }

  public void visitStringTemplate(@NotNull VlangStringTemplate o) {
    visitCompositeElement(o);
  }

  public void visitStructDeclaration(@NotNull VlangStructDeclaration o) {
    visitNamedElement(o);
  }

  public void visitStructType(@NotNull VlangStructType o) {
    visitType(o);
    // visitFieldListOwner(o);
  }

  public void visitSymbolVisibility(@NotNull VlangSymbolVisibility o) {
    visitCompositeElement(o);
  }

  public void visitTag(@NotNull VlangTag o) {
    visitCompositeElement(o);
  }

  public void visitThreadType(@NotNull VlangThreadType o) {
    visitType(o);
  }

  public void visitTupleType(@NotNull VlangTupleType o) {
    visitType(o);
  }

  public void visitType(@NotNull VlangType o) {
    visitCompositeElement(o);
  }

  public void visitTypeAliasDeclaration(@NotNull VlangTypeAliasDeclaration o) {
    visitNamedElement(o);
  }

  public void visitTypeListNoPin(@NotNull VlangTypeListNoPin o) {
    visitCompositeElement(o);
  }

  public void visitTypeReferenceExpression(@NotNull VlangTypeReferenceExpression o) {
    visitReferenceExpressionBase(o);
  }

  public void visitTypeUnionList(@NotNull VlangTypeUnionList o) {
    visitCompositeElement(o);
  }

  public void visitUnaryExpr(@NotNull VlangUnaryExpr o) {
    visitExpression(o);
  }

  public void visitUnionDeclaration(@NotNull VlangUnionDeclaration o) {
    visitNamedElement(o);
  }

  public void visitUnionType(@NotNull VlangUnionType o) {
    visitType(o);
    // visitFieldListOwner(o);
  }

  public void visitUnpackingExpression(@NotNull VlangUnpackingExpression o) {
    visitExpression(o);
  }

  public void visitUnsafeExpression(@NotNull VlangUnsafeExpression o) {
    visitExpression(o);
  }

  public void visitUnsafeStatement(@NotNull VlangUnsafeStatement o) {
    visitStatement(o);
  }

  public void visitValue(@NotNull VlangValue o) {
    visitCompositeElement(o);
  }

  public void visitVarDeclaration(@NotNull VlangVarDeclaration o) {
    visitCompositeElement(o);
  }

  public void visitVarDefinition(@NotNull VlangVarDefinition o) {
    visitMutable(o);
    // visitNamedElement(o);
  }

  public void visitVarModifier(@NotNull VlangVarModifier o) {
    visitCompositeElement(o);
  }

  public void visitVarModifiers(@NotNull VlangVarModifiers o) {
    visitCompositeElement(o);
  }

  public void visitPsiNameIdentifierOwner(@NotNull PsiNameIdentifierOwner o) {
    visitElement(o);
  }

  public void visitMutable(@NotNull VlangMutable o) {
    visitCompositeElement(o);
  }

  public void visitNamedElement(@NotNull VlangNamedElement o) {
    visitCompositeElement(o);
  }

  public void visitReferenceExpressionBase(@NotNull VlangReferenceExpressionBase o) {
    visitCompositeElement(o);
  }

  public void visitSignatureOwner(@NotNull VlangSignatureOwner o) {
    visitCompositeElement(o);
  }

  public void visitTypeOwner(@NotNull VlangTypeOwner o) {
    visitCompositeElement(o);
  }

  public void visitCompositeElement(@NotNull VlangCompositeElement o) {
    visitElement(o);
  }

}
