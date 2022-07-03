// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import org.vlang.lang.psi.VlangStringLiteral;
import org.vlang.lang.psi.VlangTag;
import org.vlang.lang.psi.VlangVisitor;

public class VlangTagImpl extends VlangCompositeElementImpl implements VlangTag {

  public VlangTagImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitTag(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public VlangStringLiteral getStringLiteral() {
    return notNullChild(VlangPsiTreeUtil.getChildOfType(this, VlangStringLiteral.class));
  }

}
