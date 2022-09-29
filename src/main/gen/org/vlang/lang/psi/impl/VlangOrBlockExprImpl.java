// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.*;

import static org.vlang.lang.VlangTypes.OR;

public class VlangOrBlockExprImpl extends VlangBinaryExprImpl implements VlangOrBlockExpr {

  public VlangOrBlockExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitOrBlockExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public VlangBlock getBlock() {
    return notNullChild(VlangPsiTreeUtil.getChildOfType(this, VlangBlock.class));
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
  @NotNull
  public PsiElement getOr() {
    return notNullChild(findChildByType(OR));
  }

}
