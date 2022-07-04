// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import org.vlang.lang.psi.VlangTypeDecl;
import org.vlang.lang.psi.VlangTypeReferenceExpression;
import org.vlang.lang.psi.VlangVisitor;

public class VlangTypeDeclImpl extends VlangCompositeElementImpl implements VlangTypeDecl {

  public VlangTypeDeclImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitTypeDecl(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangTypeReferenceExpression getTypeReferenceExpression() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangTypeReferenceExpression.class);
  }

}
