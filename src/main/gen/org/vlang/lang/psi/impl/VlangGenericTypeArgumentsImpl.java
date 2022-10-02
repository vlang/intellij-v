// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.VlangGenericTypeArguments;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import org.vlang.lang.psi.VlangTypeListNoPin;
import org.vlang.lang.psi.VlangVisitor;

import static org.vlang.lang.VlangTypes.GREATER;
import static org.vlang.lang.VlangTypes.LESS;

public class VlangGenericTypeArgumentsImpl extends VlangCompositeElementImpl implements VlangGenericTypeArguments {

  public VlangGenericTypeArgumentsImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitGenericTypeArguments(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangTypeListNoPin getTypeListNoPin() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangTypeListNoPin.class);
  }

  @Override
  @NotNull
  public PsiElement getGreater() {
    return notNullChild(findChildByType(GREATER));
  }

  @Override
  @NotNull
  public PsiElement getLess() {
    return notNullChild(findChildByType(LESS));
  }

}
