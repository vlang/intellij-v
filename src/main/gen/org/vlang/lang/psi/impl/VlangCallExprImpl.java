// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.*;

public class VlangCallExprImpl extends VlangExpressionImpl implements VlangCallExpr {

  public VlangCallExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitCallExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public VlangArgumentList getArgumentList() {
    return notNullChild(VlangPsiTreeUtil.getChildOfType(this, VlangArgumentList.class));
  }

  @Override
  @Nullable
  public VlangErrorPropagationExpression getErrorPropagationExpression() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangErrorPropagationExpression.class);
  }

  @Override
  @Nullable
  public VlangExpression getExpression() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangExpression.class);
  }

  @Override
  @Nullable
  public VlangForceNoErrorPropagationExpression getForceNoErrorPropagationExpression() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangForceNoErrorPropagationExpression.class);
  }

  @Override
  @Nullable
  public VlangGenericTypeArguments getGenericTypeArguments() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangGenericTypeArguments.class);
  }

}
