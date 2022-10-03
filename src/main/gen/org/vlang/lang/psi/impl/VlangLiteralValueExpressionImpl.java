// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.*;

import java.util.List;

import static org.vlang.lang.VlangTypes.LBRACE;
import static org.vlang.lang.VlangTypes.RBRACE;

public class VlangLiteralValueExpressionImpl extends VlangExpressionImpl implements VlangLiteralValueExpression {

  public VlangLiteralValueExpressionImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitLiteralValueExpression(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<VlangElement> getElementList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangElement.class);
  }

  @Override
  @Nullable
  public VlangGenericArguments getGenericArguments() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangGenericArguments.class);
  }

  @Override
  @NotNull
  public VlangType getType() {
    return notNullChild(VlangPsiTreeUtil.getChildOfType(this, VlangType.class));
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

}
