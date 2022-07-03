// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.vlang.lang.psi.*;

import java.util.List;

import static org.vlang.lang.VlangTypes.VAR_ASSIGN;

public class VlangShortVarDeclarationImpl extends VlangCompositeElementImpl implements VlangShortVarDeclaration {

  public VlangShortVarDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitShortVarDeclaration(this);
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

  @Override
  @NotNull
  public List<VlangVarDefinition> getVarDefinitionList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangVarDefinition.class);
  }

  @Override
  @NotNull
  public PsiElement getVarAssign() {
    return notNullChild(findChildByType(VAR_ASSIGN));
  }

}
