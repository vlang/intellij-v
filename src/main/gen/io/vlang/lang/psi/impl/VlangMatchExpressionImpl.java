// This is a generated file. Not intended for manual editing.
package io.vlang.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import io.vlang.lang.psi.VlangPsiTreeUtil;
import static io.vlang.lang.VlangTypes.*;
import io.vlang.lang.psi.*;

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
  public @NotNull List<@NotNull VlangMatchArm> getArms() {
    return VlangPsiImplUtil.getArms(this);
  }

  @Override
  public @NotNull List<@NotNull VlangExpression> getExpressionArms() {
    return VlangPsiImplUtil.getExpressionArms(this);
  }

  @Override
  public @NotNull List<@NotNull VlangType> getTypeArms() {
    return VlangPsiImplUtil.getTypeArms(this);
  }

  @Override
  public @Nullable VlangMatchElseArmClause getElseArm() {
    return VlangPsiImplUtil.getElseArm(this);
  }

  @Override
  public boolean withElse() {
    return VlangPsiImplUtil.withElse(this);
  }

}
