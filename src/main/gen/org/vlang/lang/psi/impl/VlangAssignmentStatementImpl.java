// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.vlang.lang.psi.*;

import java.util.List;

public class VlangAssignmentStatementImpl extends VlangStatementImpl implements VlangAssignmentStatement {

  public VlangAssignmentStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitAssignmentStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<VlangExpression> getExpressionList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangExpression.class);
  }

  @Override
  @NotNull
  public VlangLeftHandExprList getLeftHandExprList() {
    return notNullChild(VlangPsiTreeUtil.getChildOfType(this, VlangLeftHandExprList.class));
  }

  @Override
  @NotNull
  public VlangAssignOp getAssignOp() {
    return notNullChild(VlangPsiTreeUtil.getChildOfType(this, VlangAssignOp.class));
  }

}
