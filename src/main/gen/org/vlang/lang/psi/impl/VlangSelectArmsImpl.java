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
