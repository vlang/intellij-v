// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.*;

public class VlangSqlDeleteStatementImpl extends VlangSqlBlockStatementImpl implements VlangSqlDeleteStatement {

  public VlangSqlDeleteStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitSqlDeleteStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangReferenceExpression getReferenceExpression() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangReferenceExpression.class);
  }

  @Override
  @Nullable
  public VlangSqlFromClause getSqlFromClause() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangSqlFromClause.class);
  }

  @Override
  @Nullable
  public VlangSqlWhereClause getSqlWhereClause() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangSqlWhereClause.class);
  }

}
