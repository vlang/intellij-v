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

public class VlangOrBlockExprImpl extends VlangBinaryExprImpl implements VlangOrBlockExpr {

  public VlangOrBlockExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitOrBlockExpr(this);
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
  public VlangBlock getBlock() {
    return notNullChild(VlangPsiTreeUtil.getChildOfType(this, VlangBlock.class));
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
  public VlangOptionPropagationExpression getOptionPropagationExpression() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangOptionPropagationExpression.class);
  }

  @Override
  @Nullable
  public VlangResultPropagationExpression getResultPropagationExpression() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangResultPropagationExpression.class);
  }

  @Override
  @NotNull
  public PsiElement getOr() {
    return notNullChild(findChildByType(OR));
  }

}
