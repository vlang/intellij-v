// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.VlangArrayCreation;
import org.vlang.lang.psi.VlangArrayCreationList;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import org.vlang.lang.psi.VlangVisitor;

import static org.vlang.lang.VlangTypes.LBRACK;
import static org.vlang.lang.VlangTypes.RBRACK;

public class VlangArrayCreationImpl extends VlangExpressionImpl implements VlangArrayCreation {

  public VlangArrayCreationImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitArrayCreation(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangArrayCreationList getArrayCreationList() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangArrayCreationList.class);
  }

  @Override
  @NotNull
  public PsiElement getLbrack() {
    return notNullChild(findChildByType(LBRACK));
  }

  @Override
  @Nullable
  public PsiElement getRbrack() {
    return findChildByType(RBRACK);
  }

}
