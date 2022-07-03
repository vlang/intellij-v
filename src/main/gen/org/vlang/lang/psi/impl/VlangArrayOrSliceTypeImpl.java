// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.*;

import static org.vlang.lang.VlangTypes.*;

public class VlangArrayOrSliceTypeImpl extends VlangTypeImpl implements VlangArrayOrSliceType {

  public VlangArrayOrSliceTypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitArrayOrSliceType(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangExpression getExpression() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangExpression.class);
  }

  @Override
  @Nullable
  public VlangType getType() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangType.class);
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

  @Override
  @Nullable
  public PsiElement getTripleDot() {
    return findChildByType(TRIPLE_DOT);
  }

}
