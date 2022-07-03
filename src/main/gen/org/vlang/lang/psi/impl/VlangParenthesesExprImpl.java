// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.VlangExpression;
import org.vlang.lang.psi.VlangParenthesesExpr;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import org.vlang.lang.psi.VlangVisitor;

import static org.vlang.lang.VlangTypes.LPAREN;
import static org.vlang.lang.VlangTypes.RPAREN;

public class VlangParenthesesExprImpl extends VlangExpressionImpl implements VlangParenthesesExpr {

  public VlangParenthesesExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitParenthesesExpr(this);
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
  @NotNull
  public PsiElement getLparen() {
    return notNullChild(findChildByType(LPAREN));
  }

  @Override
  @Nullable
  public PsiElement getRparen() {
    return findChildByType(RPAREN);
  }

}
