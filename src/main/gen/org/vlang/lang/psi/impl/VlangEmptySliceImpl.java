// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.vlang.lang.psi.VlangEmptySlice;
import org.vlang.lang.psi.VlangVisitor;

import static org.vlang.lang.VlangTypes.RANGE;

public class VlangEmptySliceImpl extends VlangCompositeElementImpl implements VlangEmptySlice {

  public VlangEmptySliceImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitEmptySlice(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiElement getRange() {
    return notNullChild(findChildByType(RANGE));
  }

}
