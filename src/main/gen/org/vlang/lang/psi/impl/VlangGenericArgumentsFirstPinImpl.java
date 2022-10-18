// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.VlangGenericArgumentsFirstPin;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import org.vlang.lang.psi.VlangTypeListNoPin;
import org.vlang.lang.psi.VlangVisitor;

import static org.vlang.lang.VlangTypes.GREATER;
import static org.vlang.lang.VlangTypes.LESS;

public class VlangGenericArgumentsFirstPinImpl extends VlangCompositeElementImpl implements VlangGenericArgumentsFirstPin {

  public VlangGenericArgumentsFirstPinImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitGenericArgumentsFirstPin(this);
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
  @Nullable
  public PsiElement getGreater() {
    return findChildByType(GREATER);
  }

  @Override
  @NotNull
  public PsiElement getLess() {
    return notNullChild(findChildByType(LESS));
  }

}
