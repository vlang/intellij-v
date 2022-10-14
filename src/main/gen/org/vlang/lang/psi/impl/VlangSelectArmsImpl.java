// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.vlang.lang.psi.*;

import java.util.List;

public class VlangSelectArmsImpl extends VlangCompositeElementImpl implements VlangSelectArms {

  public VlangSelectArmsImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitSelectArms(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<VlangSelectArm> getSelectArmList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangSelectArm.class);
  }

  @Override
  @NotNull
  public List<VlangSelectElseArmClause> getSelectElseArmClauseList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangSelectElseArmClause.class);
  }

}
