// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.VlangBlock;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import org.vlang.lang.psi.VlangUnsafeExpression;
import org.vlang.lang.psi.VlangVisitor;

import static org.vlang.lang.VlangTypes.UNSAFE;

public class VlangUnsafeExpressionImpl extends VlangExpressionImpl implements VlangUnsafeExpression {

  public VlangUnsafeExpressionImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitUnsafeExpression(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangBlock getBlock() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangBlock.class);
  }

  @Override
  @NotNull
  public PsiElement getUnsafe() {
    return notNullChild(findChildByType(UNSAFE));
  }

}
