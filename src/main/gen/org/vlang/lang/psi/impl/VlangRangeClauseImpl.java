// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.*;

import java.util.List;

import static org.vlang.lang.VlangTypes.IN;
import static org.vlang.lang.VlangTypes.NOT_IN;

public class VlangRangeClauseImpl extends VlangShortVarDeclarationImpl implements VlangRangeClause {

  public VlangRangeClauseImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitRangeClause(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangExpression getExpression() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangExpression.class);
  }

  @Override
  @NotNull
  public List<VlangVarDefinition> getVarDefinitionList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangVarDefinition.class);
  }

  @Override
  @Nullable
  public PsiElement getNotIn() {
    return findChildByType(NOT_IN);
  }

  @Override
  @Nullable
  public PsiElement getIn() {
    return findChildByType(IN);
  }

}
