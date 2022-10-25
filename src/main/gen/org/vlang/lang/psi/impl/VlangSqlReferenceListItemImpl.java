// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.*;

public class VlangSqlReferenceListItemImpl extends VlangCompositeElementImpl implements VlangSqlReferenceListItem {

  public VlangSqlReferenceListItemImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitSqlReferenceListItem(this);
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
  public VlangSqlLimitClause getSqlLimitClause() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangSqlLimitClause.class);
  }

  @Override
  @Nullable
  public VlangSqlOffsetClause getSqlOffsetClause() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangSqlOffsetClause.class);
  }

}
