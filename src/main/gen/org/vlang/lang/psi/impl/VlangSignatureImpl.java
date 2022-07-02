// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.*;

public class VlangSignatureImpl extends VlangCompositeElementImpl implements VlangSignature {

  public VlangSignatureImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitSignature(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public VlangParameters getParameters() {
    return notNullChild(VlangPsiTreeUtil.getChildOfType(this, VlangParameters.class));
  }

  @Override
  @Nullable
  public VlangResult getResult() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangResult.class);
  }

}
