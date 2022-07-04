// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.*;

import java.util.List;

import static org.vlang.lang.VlangTypes.LPAREN;
import static org.vlang.lang.VlangTypes.RPAREN;

public class VlangParametersImpl extends VlangCompositeElementImpl implements VlangParameters {

  public VlangParametersImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitParameters(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<VlangParameterDeclaration> getParameterDeclarationList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangParameterDeclaration.class);
  }

  @Override
  @Nullable
  public VlangTypeDecl getTypeDecl() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangTypeDecl.class);
  }

  @Override
  @NotNull
  public PsiElement getLparen() {
    return notNullChild(findChildByType(LPAREN));
  }

  @Override
  @Nullable
  public PsiElement getRparen() {
    return findChildByType(RPAREN);
  }

}
