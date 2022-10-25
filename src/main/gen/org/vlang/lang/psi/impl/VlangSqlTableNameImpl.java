// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import org.vlang.lang.psi.VlangSqlTableName;
import org.vlang.lang.psi.VlangTypeReferenceExpression;
import org.vlang.lang.psi.VlangVisitor;

public class VlangSqlTableNameImpl extends VlangCompositeElementImpl implements VlangSqlTableName {

  public VlangSqlTableNameImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitSqlTableName(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public VlangTypeReferenceExpression getTypeReferenceExpression() {
    return notNullChild(VlangPsiTreeUtil.getChildOfType(this, VlangTypeReferenceExpression.class));
  }

}
