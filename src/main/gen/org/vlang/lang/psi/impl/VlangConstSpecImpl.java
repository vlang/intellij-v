// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.*;

import java.util.List;

import static org.vlang.lang.VlangTypes.ASSIGN;

public class VlangConstSpecImpl extends VlangCompositeElementImpl implements VlangConstSpec {

  public VlangConstSpecImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitConstSpec(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<VlangConstDefinition> getConstDefinitionList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangConstDefinition.class);
  }

  @Override
  @NotNull
  public List<VlangExpression> getExpressionList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangExpression.class);
  }

  @Override
  @Nullable
  public VlangType getType() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangType.class);
  }

  @Override
  @Nullable
  public PsiElement getAssign() {
    return findChildByType(ASSIGN);
  }

}
