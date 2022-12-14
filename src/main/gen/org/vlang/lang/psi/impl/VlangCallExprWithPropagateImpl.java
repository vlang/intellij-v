// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.VlangCallExprWithPropagate;
import org.vlang.lang.psi.VlangVisitor;

import static org.vlang.lang.VlangTypes.NOT;
import static org.vlang.lang.VlangTypes.QUESTION;

public class VlangCallExprWithPropagateImpl extends VlangCallExprImpl implements VlangCallExprWithPropagate {

  public VlangCallExprWithPropagateImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitCallExprWithPropagate(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiElement getNot() {
    return findChildByType(NOT);
  }

  @Override
  @Nullable
  public PsiElement getQuestion() {
    return findChildByType(QUESTION);
  }

}
