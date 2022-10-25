// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import org.vlang.lang.psi.VlangSqlOrderByClause;
import org.vlang.lang.psi.VlangSqlReferenceList;
import org.vlang.lang.psi.VlangVisitor;

public class VlangSqlOrderByClauseImpl extends VlangCompositeElementImpl implements VlangSqlOrderByClause {

  public VlangSqlOrderByClauseImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitSqlOrderByClause(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangSqlReferenceList getSqlReferenceList() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangSqlReferenceList.class);
  }

}
