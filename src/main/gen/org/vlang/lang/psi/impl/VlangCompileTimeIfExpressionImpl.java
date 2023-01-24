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

public class VlangCompileTimeIfExpressionImpl extends VlangExpressionImpl implements VlangCompileTimeIfExpression {

  public VlangCompileTimeIfExpressionImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitCompileTimeIfExpression(this);
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
  public VlangCompileTimeElseBranch getCompileTimeElseBranch() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangCompileTimeElseBranch.class);
  }

  @Override
  @Nullable
  public VlangExpression getExpression() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangExpression.class);
  }

  @Override
  @Nullable
  public VlangGuardVarDeclaration getGuardVarDeclaration() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangGuardVarDeclaration.class);
  }

  @Override
  @NotNull
  public PsiElement getIfCompileTime() {
    return notNullChild(findChildByType(IF_COMPILE_TIME));
  }

}
