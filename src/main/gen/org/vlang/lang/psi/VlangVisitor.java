// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiLanguageInjectionHost;

public class VlangVisitor extends PsiElementVisitor {

  public void visitAddExpr(@NotNull VlangAddExpr o) {
    visitBinaryExpr(o);
  }

  public void visitAliasType(@NotNull VlangAliasType o) {
    visitType(o);
    // visitGenericParametersOwner(o);
  }

  public void visitAndExpr(@NotNull VlangAndExpr o) {
    visitBinaryExpr(o);
  }

  public void visitAnonymousStructType(@NotNull VlangAnonymousStructType o) {
    visitType(o);
    // visitFieldListOwner(o);
  }

  public void visitAnonymousStructValueExpression(@NotNull VlangAnonymousStructValueExpression o) {
    visitCompositeElement(o);
  }

  public void visitAppendStatement(@NotNull VlangAppendStatement o) {
    visitStatement(o);
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

  public void visitArrayType(@NotNull VlangArrayType o) {
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

  public void visitAtomicType(@NotNull VlangAtomicType o) {
    visitType(o);
  }

  public void visitAttribute(@NotNull VlangAttribute o) {
    visitCompositeElement(o);
  }

  public void visitAttributeExpression(@NotNull VlangAttributeExpression o) {
    visitCompositeElement(o);
  }

  public void visitAttributeIdentifier(@NotNull VlangAttributeIdentifier o) {
    visitCompositeElement(o);
  }

  public void visitAttributeIdentifierPrefix(@NotNull VlangAttributeIdentifierPrefix o) {
    visitCompositeElement(o);
  }

  public void visitAttributeKey(@NotNull VlangAttributeKey o) {
    visitCompositeElement(o);
  }

  public void visitAttributeValue(@NotNull VlangAttributeValue o) {
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

  public void visitCallExpr(@NotNull VlangCallExpr o) {
    visitExpression(o);
    // visitGenericArgumentsOwner(o);
  }

  public void visitCallExprWithPropagate(@NotNull VlangCallExprWithPropagate o) {
    visitCallExpr(o);
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

  public void visitCompileTimeElseBranch(@NotNull VlangCompileTimeElseBranch o) {
    visitCompositeElement(o);
  }

  public void visitCompileTimeFieldReference(@NotNull VlangCompileTimeFieldReference o) {
    visitCompositeElement(o);
  }

  public void visitCompileTimeForStatement(@NotNull VlangCompileTimeForStatement o) {
    visitStatement(o);
  }

  public void visitCompileTimeIfExpression(@NotNull VlangCompileTimeIfExpression o) {
    visitExpression(o);
  }

  public void visitConditionalExpr(@NotNull VlangConditionalExpr o) {
    visitBinaryExpr(o);
  }

  public void visitConstDeclaration(@NotNull VlangConstDeclaration o) {
    visitAttributeOwner(o);
  }

  public void visitConstDefinition(@NotNull VlangConstDefinition o) {
    visitNamedElement(o);
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

  public void visitDumpCallExpr(@NotNull VlangDumpCallExpr o) {
    visitExpression(o);
    // visitBuiltinCallOwner(o);
  }

  public void visitElement(@NotNull VlangElement o) {
    visitCompositeElement(o);
  }

  public void visitElseBranch(@NotNull VlangElseBranch o) {
    visitCompositeElement(o);
  }

  public void visitEmbeddedDefinition(@NotNull VlangEmbeddedDefinition o) {
    visitNamedElement(o);
  }

  public void visitEmbeddedInterfaceDefinition(@NotNull VlangEmbeddedInterfaceDefinition o) {
    visitCompositeElement(o);
  }

  public void visitEmptySlice(@NotNull VlangEmptySlice o) {
    visitCompositeElement(o);
  }

  public void visitEnumBackedTypeAs(@NotNull VlangEnumBackedTypeAs o) {
    visitCompositeElement(o);
  }

  public void visitEnumDeclaration(@NotNull VlangEnumDeclaration o) {
    visitNamedElement(o);
    // visitAttributeOwner(o);
  }

  public void visitEnumFetch(@NotNull VlangEnumFetch o) {
    visitExpression(o);
    // visitReferenceExpressionBase(o);
  }

  public void visitEnumFieldDeclaration(@NotNull VlangEnumFieldDeclaration o) {
    visitCompositeElement(o);
  }

  public void visitEnumFieldDefinition(@NotNull VlangEnumFieldDefinition o) {
    visitNamedElement(o);
  }

  public void visitEnumType(@NotNull VlangEnumType o) {
    visitType(o);
  }

  public void visitExpression(@NotNull VlangExpression o) {
    visitTypeOwner(o);
  }

  public void visitFieldDeclaration(@NotNull VlangFieldDeclaration o) {
    visitCompositeElement(o);
  }

  public void visitFieldDefinition(@NotNull VlangFieldDefinition o) {
    visitMutabilityOwner(o);
    // visitNamedElement(o);
  }

  public void visitFieldName(@NotNull VlangFieldName o) {
    visitReferenceExpressionBase(o);
  }

  public void visitFieldsGroup(@NotNull VlangFieldsGroup o) {
    visitMemberModifiersOwner(o);
  }

  public void visitFixedSizeArrayType(@NotNull VlangFixedSizeArrayType o) {
    visitType(o);
  }

  public void visitForClause(@NotNull VlangForClause o) {
    visitCompositeElement(o);
  }

  public void visitForStatement(@NotNull VlangForStatement o) {
    visitStatement(o);
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
    // visitAttributeOwner(o);
    // visitGenericParametersOwner(o);
    // visitScopeHolder(o);
  }

  public void visitFunctionLit(@NotNull VlangFunctionLit o) {
    visitExpression(o);
    // visitSignatureOwner(o);
    // visitGenericParametersOwner(o);
  }

  public void visitFunctionType(@NotNull VlangFunctionType o) {
    visitType(o);
    // visitSignatureOwner(o);
  }

  public void visitGenericArguments(@NotNull VlangGenericArguments o) {
    visitCompositeElement(o);
  }

  public void visitGenericParameter(@NotNull VlangGenericParameter o) {
    visitNamedElement(o);
  }

  public void visitGenericParameterList(@NotNull VlangGenericParameterList o) {
    visitCompositeElement(o);
  }

  public void visitGenericParameters(@NotNull VlangGenericParameters o) {
    visitCompositeElement(o);
  }

  public void visitGlobalVariableDeclaration(@NotNull VlangGlobalVariableDeclaration o) {
    visitAttributeOwner(o);
  }

  public void visitGlobalVariableDefinition(@NotNull VlangGlobalVariableDefinition o) {
    visitNamedElement(o);
  }

  public void visitGoExpression(@NotNull VlangGoExpression o) {
    visitExpression(o);
  }

  public void visitGotoStatement(@NotNull VlangGotoStatement o) {
    visitStatement(o);
    // visitLabelRefOwnerElement(o);
  }

  public void visitGuardVarDeclaration(@NotNull VlangGuardVarDeclaration o) {
    visitVarDeclaration(o);
  }

  public void visitIfAttribute(@NotNull VlangIfAttribute o) {
    visitCompositeElement(o);
  }

  public void visitIfExpression(@NotNull VlangIfExpression o) {
    visitExpression(o);
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
    visitNamedElement(o);
  }

  public void visitImportPath(@NotNull VlangImportPath o) {
    visitCompositeElement(o);
  }

  public void visitImportSpec(@NotNull VlangImportSpec o) {
    visitCompositeElement(o);
  }

  public void visitInExpression(@NotNull VlangInExpression o) {
    visitBinaryExpr(o);
  }

  public void visitIncDecExpression(@NotNull VlangIncDecExpression o) {
    visitExpression(o);
  }

  public void visitIndexOrSliceExpr(@NotNull VlangIndexOrSliceExpr o) {
    visitExpression(o);
  }

  public void visitInterfaceDeclaration(@NotNull VlangInterfaceDeclaration o) {
    visitNamedElement(o);
    // visitAttributeOwner(o);
  }

  public void visitInterfaceMethodDeclaration(@NotNull VlangInterfaceMethodDeclaration o) {
    visitCompositeElement(o);
  }

  public void visitInterfaceMethodDefinition(@NotNull VlangInterfaceMethodDefinition o) {
    visitSignatureOwner(o);
    // visitNamedElement(o);
    // visitGenericParametersOwner(o);
  }

  public void visitInterfaceType(@NotNull VlangInterfaceType o) {
    visitType(o);
    // visitFieldListOwner(o);
    // visitGenericParametersOwner(o);
  }

  public void visitIsExpression(@NotNull VlangIsExpression o) {
    visitBinaryExpr(o);
  }

  public void visitIsRefTypeCallExpr(@NotNull VlangIsRefTypeCallExpr o) {
    visitExpression(o);
    // visitBuiltinCallOwner(o);
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

  public void visitLockParts(@NotNull VlangLockParts o) {
    visitCompositeElement(o);
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
    visitMemberModifiersOwner(o);
  }

  public void visitMethodDeclaration(@NotNull VlangMethodDeclaration o) {
    visitSignatureOwner(o);
    // visitFunctionOrMethodDeclaration(o);
    // visitAttributeOwner(o);
    // visitGenericParametersOwner(o);
    // visitScopeHolder(o);
  }

  public void visitMethodName(@NotNull VlangMethodName o) {
    visitCompositeElement(o);
  }

  public void visitModuleClause(@NotNull VlangModuleClause o) {
    visitNamedElement(o);
    // visitAttributeOwner(o);
  }

  public void visitMulExpr(@NotNull VlangMulExpr o) {
    visitBinaryExpr(o);
  }

  public void visitMutExpression(@NotNull VlangMutExpression o) {
    visitExpression(o);
  }

  public void visitNoneType(@NotNull VlangNoneType o) {
    visitType(o);
  }

  public void visitNotInExpression(@NotNull VlangNotInExpression o) {
    visitBinaryExpr(o);
  }

  public void visitNotIsExpression(@NotNull VlangNotIsExpression o) {
    visitBinaryExpr(o);
  }

  public void visitOffsetOfCallExpr(@NotNull VlangOffsetOfCallExpr o) {
    visitExpression(o);
    // visitBuiltinCallOwner(o);
  }

  public void visitOptionPropagationExpression(@NotNull VlangOptionPropagationExpression o) {
    visitCompositeElement(o);
  }

  public void visitOptionType(@NotNull VlangOptionType o) {
    visitType(o);
  }

  public void visitOrBlockExpr(@NotNull VlangOrBlockExpr o) {
    visitBinaryExpr(o);
  }

  public void visitOrExpr(@NotNull VlangOrExpr o) {
    visitBinaryExpr(o);
  }

  public void visitParamDefinition(@NotNull VlangParamDefinition o) {
    visitMutabilityOwner(o);
    // visitNamedElement(o);
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
    visitMutabilityOwner(o);
    // visitNamedElement(o);
  }

  public void visitReferenceExpression(@NotNull VlangReferenceExpression o) {
    visitExpression(o);
    // visitReferenceExpressionBase(o);
  }

  public void visitResult(@NotNull VlangResult o) {
    visitCompositeElement(o);
  }

  public void visitResultPropagationExpression(@NotNull VlangResultPropagationExpression o) {
    visitCompositeElement(o);
  }

  public void visitResultType(@NotNull VlangResultType o) {
    visitType(o);
  }

  public void visitReturnStatement(@NotNull VlangReturnStatement o) {
    visitStatement(o);
  }

  public void visitSelectArm(@NotNull VlangSelectArm o) {
    visitCompositeElement(o);
  }

  public void visitSelectArmAssignmentStatement(@NotNull VlangSelectArmAssignmentStatement o) {
    visitAssignmentStatement(o);
  }

  public void visitSelectArmStatement(@NotNull VlangSelectArmStatement o) {
    visitStatement(o);
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

  public void visitSharedType(@NotNull VlangSharedType o) {
    visitType(o);
  }

  public void visitShebangClause(@NotNull VlangShebangClause o) {
    visitCompositeElement(o);
  }

  public void visitShiftLeftExpr(@NotNull VlangShiftLeftExpr o) {
    visitBinaryExpr(o);
  }

  public void visitShiftLeftOp(@NotNull VlangShiftLeftOp o) {
    visitCompositeElement(o);
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

  public void visitSizeOfCallExpr(@NotNull VlangSizeOfCallExpr o) {
    visitExpression(o);
    // visitBuiltinCallOwner(o);
  }

  public void visitSpawnExpression(@NotNull VlangSpawnExpression o) {
    visitExpression(o);
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

  public void visitStringTemplateEntry(@NotNull VlangStringTemplateEntry o) {
    visitCompositeElement(o);
  }

  public void visitStructDeclaration(@NotNull VlangStructDeclaration o) {
    visitNamedElement(o);
    // visitAttributeOwner(o);
  }

  public void visitStructType(@NotNull VlangStructType o) {
    visitType(o);
    // visitFieldListOwner(o);
    // visitGenericParametersOwner(o);
  }

  public void visitSymbolVisibility(@NotNull VlangSymbolVisibility o) {
    visitCompositeElement(o);
  }

  public void visitThreadType(@NotNull VlangThreadType o) {
    visitType(o);
  }

  public void visitTupleType(@NotNull VlangTupleType o) {
    visitType(o);
  }

  public void visitType(@NotNull VlangType o) {
    visitGenericArgumentsOwner(o);
  }

  public void visitTypeAliasDeclaration(@NotNull VlangTypeAliasDeclaration o) {
    visitNamedElement(o);
    // visitAttributeOwner(o);
  }

  public void visitTypeCastExpression(@NotNull VlangTypeCastExpression o) {
    visitExpression(o);
  }

  public void visitTypeListNoPin(@NotNull VlangTypeListNoPin o) {
    visitCompositeElement(o);
  }

  public void visitTypeModifier(@NotNull VlangTypeModifier o) {
    visitCompositeElement(o);
  }

  public void visitTypeModifiers(@NotNull VlangTypeModifiers o) {
    visitCompositeElement(o);
  }

  public void visitTypeOfCallExpr(@NotNull VlangTypeOfCallExpr o) {
    visitExpression(o);
    // visitBuiltinCallOwner(o);
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

  public void visitUnfinishedMemberModifiers(@NotNull VlangUnfinishedMemberModifiers o) {
    visitCompositeElement(o);
  }

  public void visitUnpackingExpression(@NotNull VlangUnpackingExpression o) {
    visitExpression(o);
  }

  public void visitUnsafeExpression(@NotNull VlangUnsafeExpression o) {
    visitExpression(o);
  }

  public void visitValue(@NotNull VlangValue o) {
    visitCompositeElement(o);
  }

  public void visitVarDeclaration(@NotNull VlangVarDeclaration o) {
    visitCompositeElement(o);
  }

  public void visitVarDefinition(@NotNull VlangVarDefinition o) {
    visitMutabilityOwner(o);
    // visitNamedElement(o);
  }

  public void visitVarModifier(@NotNull VlangVarModifier o) {
    visitCompositeElement(o);
  }

  public void visitVarModifiers(@NotNull VlangVarModifiers o) {
    visitCompositeElement(o);
  }

  public void visitWrongPointerType(@NotNull VlangWrongPointerType o) {
    visitType(o);
  }

  public void visitAttributeOwner(@NotNull VlangAttributeOwner o) {
    visitCompositeElement(o);
  }

  public void visitGenericArgumentsOwner(@NotNull VlangGenericArgumentsOwner o) {
    visitCompositeElement(o);
  }

  public void visitMemberModifiersOwner(@NotNull VlangMemberModifiersOwner o) {
    visitCompositeElement(o);
  }

  public void visitMutabilityOwner(@NotNull VlangMutabilityOwner o) {
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
