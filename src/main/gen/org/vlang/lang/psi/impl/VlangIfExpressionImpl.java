// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.*;

import static org.vlang.lang.VlangTypes.IF;
import static org.vlang.lang.VlangTypes.SEMICOLON;

public class VlangIfExpressionImpl extends VlangExpressionImpl implements VlangIfExpression {

  public VlangIfExpressionImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitIfExpression(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangBlock getBlock() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangBlock.class);
  }

  @Override
  @Nullable
  public VlangElseStatement getElseStatement() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangElseStatement.class);
  }

  @Override
  @Nullable
  public VlangErrorPropagation getErrorPropagation() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangErrorPropagation.class);
  }

  @Override
  @Nullable
  public VlangExpression getExpression() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangExpression.class);
  }

  @Override
  @Nullable
  public VlangStatement getStatement() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangStatement.class);
  }

  @Override
  @Nullable
  public PsiElement getSemicolon() {
    return findChildByType(SEMICOLON);
  }

  @Override
  @NotNull
  public PsiElement getIf() {
    return notNullChild(findChildByType(IF));
  }

}
