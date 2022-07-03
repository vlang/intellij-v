// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.vlang.lang.psi.*;

import java.util.List;

public class VlangMatchArmsImpl extends VlangCompositeElementImpl implements VlangMatchArms {

  public VlangMatchArmsImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitMatchArms(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<VlangMatchArm> getMatchArmList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangMatchArm.class);
  }

  @Override
  @NotNull
  public List<VlangMatchElseArmClause> getMatchElseArmClauseList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangMatchElseArmClause.class);
  }

}
