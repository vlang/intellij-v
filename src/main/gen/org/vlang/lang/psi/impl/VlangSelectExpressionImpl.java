// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import org.vlang.lang.psi.VlangSelectArms;
import org.vlang.lang.psi.VlangSelectExpression;
import org.vlang.lang.psi.VlangVisitor;

import static org.vlang.lang.VlangTypes.*;

public class VlangSelectExpressionImpl extends VlangExpressionImpl implements VlangSelectExpression {

  public VlangSelectExpressionImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitSelectExpression(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangSelectArms getSelectArms() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangSelectArms.class);
  }

  @Override
  @NotNull
  public PsiElement getLbrace() {
    return notNullChild(findChildByType(LBRACE));
  }

  @Override
  @Nullable
  public PsiElement getRbrace() {
    return findChildByType(RBRACE);
  }

  @Override
  @NotNull
  public PsiElement getSelect() {
    return notNullChild(findChildByType(SELECT));
  }

}
