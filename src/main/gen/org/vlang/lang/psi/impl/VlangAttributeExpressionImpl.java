// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.*;

public class VlangAttributeExpressionImpl extends VlangCompositeElementImpl implements VlangAttributeExpression {

  public VlangAttributeExpressionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitAttributeExpression(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangIfAttribute getIfAttribute() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangIfAttribute.class);
  }

  @Override
  @Nullable
  public VlangPlainAttribute getPlainAttribute() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangPlainAttribute.class);
  }

}
