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

public class VlangCompileTimeElseBranchImpl extends VlangCompositeElementImpl implements VlangCompileTimeElseBranch {

  public VlangCompileTimeElseBranchImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitCompileTimeElseBranch(this);
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
  public VlangCompileTimeIfExpression getCompileTimeIfExpression() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangCompileTimeIfExpression.class);
  }

  @Override
  @NotNull
  public PsiElement getElseCompileTime() {
    return notNullChild(findChildByType(ELSE_COMPILE_TIME));
  }

}
