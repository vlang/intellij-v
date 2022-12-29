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

public class VlangMatchExpressionImpl extends VlangExpressionImpl implements VlangMatchExpression {

  public VlangMatchExpressionImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitMatchExpression(this);
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
  public VlangMatchArms getMatchArms() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangMatchArms.class);
  }

  @Override
  @Nullable
  public PsiElement getLbrace() {
    return findChildByType(LBRACE);
  }

  @Override
  @Nullable
  public PsiElement getRbrace() {
    return findChildByType(RBRACE);
  }

  @Override
  @NotNull
  public PsiElement getMatch() {
    return notNullChild(findChildByType(MATCH));
  }

  @Override
  @NotNull
  public List<VlangMatchArm> getArms() {
    return VlangPsiImplUtil.getArms(this);
  }

  @Override
  @NotNull
  public List<VlangExpression> getExpressionArms() {
    return VlangPsiImplUtil.getExpressionArms(this);
  }

  @Override
  @NotNull
  public List<VlangType> getTypeArms() {
    return VlangPsiImplUtil.getTypeArms(this);
  }

  @Override
  @Nullable
  public VlangMatchElseArmClause getElseArm() {
    return VlangPsiImplUtil.getElseArm(this);
  }

  @Override
  public boolean withElse() {
    return VlangPsiImplUtil.withElse(this);
  }

}
