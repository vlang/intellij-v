// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.vlang.lang.psi.VlangArrayCreationList;
import org.vlang.lang.psi.VlangExpression;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import org.vlang.lang.psi.VlangVisitor;

import java.util.List;

public class VlangArrayCreationListImpl extends VlangCompositeElementImpl implements VlangArrayCreationList {

  public VlangArrayCreationListImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitArrayCreationList(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<VlangExpression> getExpressionList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangExpression.class);
  }

}
