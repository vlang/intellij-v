// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.*;

public class VlangCaptureImpl extends VlangCompositeElementImpl implements VlangCapture {

  public VlangCaptureImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitCapture(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public VlangReferenceExpression getReferenceExpression() {
    return notNullChild(VlangPsiTreeUtil.getChildOfType(this, VlangReferenceExpression.class));
  }

  @Override
  @Nullable
  public VlangVarModifiers getVarModifiers() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangVarModifiers.class);
  }

}
