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

public class VlangOffsetOfCallExprImpl extends VlangExpressionImpl implements VlangOffsetOfCallExpr {

  public VlangOffsetOfCallExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitOffsetOfCallExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangReferenceExpression getReferenceExpression() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangReferenceExpression.class);
  }

  @Override
  @Nullable
  public VlangType getType() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangType.class);
  }

  @Override
  @Nullable
  public PsiElement getComma() {
    return findChildByType(COMMA);
  }

  @Override
  @Nullable
  public PsiElement getLparen() {
    return findChildByType(LPAREN);
  }

  @Override
  @Nullable
  public PsiElement getRparen() {
    return findChildByType(RPAREN);
  }

  @Override
  @NotNull
  public PsiElement getOffsetof() {
    return notNullChild(findChildByType(OFFSETOF));
  }

  @Override
  @NotNull
  public VlangBuiltinReference<VlangOffsetOfCallExpr> getReference() {
    return VlangPsiImplUtil.getReference(this);
  }

}
