// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import org.vlang.lang.psi.VlangSqlDropStatement;
import org.vlang.lang.psi.VlangSqlTableName;
import org.vlang.lang.psi.VlangVisitor;

public class VlangSqlDropStatementImpl extends VlangSqlBlockStatementImpl implements VlangSqlDropStatement {

  public VlangSqlDropStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitSqlDropStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangSqlTableName getSqlTableName() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangSqlTableName.class);
  }

}
