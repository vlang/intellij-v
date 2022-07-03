// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.VlangNullableType;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import org.vlang.lang.psi.VlangType;
import org.vlang.lang.psi.VlangVisitor;

import static org.vlang.lang.VlangTypes.QUESTION;

public class VlangNullableTypeImpl extends VlangTypeImpl implements VlangNullableType {

  public VlangNullableTypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitNullableType(this);
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
  public PsiElement getQuestion() {
    return notNullChild(findChildByType(QUESTION));
  }

}
