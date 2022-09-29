// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.*;

public class VlangDotExpressionImpl extends VlangExpressionImpl implements VlangDotExpression {

  public VlangDotExpressionImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitDotExpression(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangErrorPropagationExpression getErrorPropagationExpression() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangErrorPropagationExpression.class);
  }

  @Override
  @NotNull
  public VlangExpression getExpression() {
    return notNullChild(VlangPsiTreeUtil.getChildOfType(this, VlangExpression.class));
  }

  @Override
  @Nullable
  public VlangForceNoErrorPropagationExpression getForceNoErrorPropagationExpression() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangForceNoErrorPropagationExpression.class);
  }

}
