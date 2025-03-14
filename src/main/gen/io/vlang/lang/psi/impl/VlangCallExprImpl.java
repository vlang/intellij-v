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

public class VlangCallExprImpl extends VlangExpressionImpl implements VlangCallExpr {

  public VlangCallExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitCallExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangAnonymousStructValueExpression getAnonymousStructValueExpression() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangAnonymousStructValueExpression.class);
  }

  @Override
  @NotNull
  public VlangArgumentList getArgumentList() {
    return notNullChild(VlangPsiTreeUtil.getChildOfType(this, VlangArgumentList.class));
  }

  @Override
  @Nullable
  public VlangCompileTimeFieldReference getCompileTimeFieldReference() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangCompileTimeFieldReference.class);
  }

  @Override
  @Nullable
  public VlangExpression getExpression() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangExpression.class);
  }

  @Override
  @Nullable
  public VlangGenericArguments getGenericArguments() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangGenericArguments.class);
  }

  @Override
  @Nullable
  public VlangOptionPropagationExpression getOptionPropagationExpression() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangOptionPropagationExpression.class);
  }

  @Override
  @Nullable
  public VlangResultPropagationExpression getResultPropagationExpression() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangResultPropagationExpression.class);
  }

  @Override
  public @NotNull List<@NotNull VlangExpression> getParameters() {
    return VlangPsiImplUtil.getParameters(this);
  }

  @Override
  public @Nullable PsiElement getIdentifier() {
    return VlangPsiImplUtil.getIdentifier(this);
  }

  @Override
  public @Nullable VlangReferenceExpression getQualifier() {
    return VlangPsiImplUtil.getQualifier(this);
  }

  @Override
  public @Nullable PsiElement resolve() {
    return VlangPsiImplUtil.resolve(this);
  }

  @Override
  public int paramIndexOf(@NotNull PsiElement pos) {
    return VlangPsiImplUtil.paramIndexOf(this, pos);
  }

}
