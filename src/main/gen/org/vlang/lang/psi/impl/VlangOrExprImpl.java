// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.vlang.lang.psi.VlangOrExpr;
import org.vlang.lang.psi.VlangVisitor;

import static org.vlang.lang.VlangTypes.COND_OR;

public class VlangOrExprImpl extends VlangBinaryExprImpl implements VlangOrExpr {

  public VlangOrExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitOrExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiElement getCondOr() {
    return notNullChild(findChildByType(COND_OR));
  }

}
