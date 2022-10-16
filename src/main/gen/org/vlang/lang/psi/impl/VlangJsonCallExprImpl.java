// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.vlang.lang.psi.*;

import java.util.List;

public class VlangJsonCallExprImpl extends VlangCallExprImpl implements VlangJsonCallExpr {

  public VlangJsonCallExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitJsonCallExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public VlangJsonArgumentList getJsonArgumentList() {
    return notNullChild(VlangPsiTreeUtil.getChildOfType(this, VlangJsonArgumentList.class));
  }

  @Override
  @NotNull
  public List<VlangExpression> getParameters() {
    return VlangPsiImplUtil.getParameters(this);
  }

}
