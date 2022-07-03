// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.VlangPointerType;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import org.vlang.lang.psi.VlangType;
import org.vlang.lang.psi.VlangVisitor;

import static org.vlang.lang.VlangTypes.BIT_AND;

public class VlangPointerTypeImpl extends VlangTypeImpl implements VlangPointerType {

  public VlangPointerTypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitPointerType(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangType getType() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangType.class);
  }

  @Override
  @NotNull
  public PsiElement getBitAnd() {
    return notNullChild(findChildByType(BIT_AND));
  }

}
