// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import static org.vlang.lang.VlangTypes.*;
import org.vlang.lang.psi.*;

public class VlangConditionalExprImpl extends VlangBinaryExprImpl implements VlangConditionalExpr {

  public VlangConditionalExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitConditionalExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiElement getEq() {
    return findChildByType(EQ);
  }

  @Override
  @Nullable
  public PsiElement getGreater() {
    return findChildByType(GREATER);
  }

  @Override
  @Nullable
  public PsiElement getGreaterOrEqual() {
    return findChildByType(GREATER_OR_EQUAL);
  }

  @Override
  @Nullable
  public PsiElement getLess() {
    return findChildByType(LESS);
  }

  @Override
  @Nullable
  public PsiElement getLessOrEqual() {
    return findChildByType(LESS_OR_EQUAL);
  }

  @Override
  @Nullable
  public PsiElement getNotEq() {
    return findChildByType(NOT_EQ);
  }

}
