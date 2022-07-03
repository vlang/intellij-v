// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.*;

import java.util.List;

import static org.vlang.lang.VlangTypes.*;

public class VlangConstDeclarationImpl extends VlangCompositeElementImpl implements VlangConstDeclaration {

  public VlangConstDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitConstDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<VlangConstSpec> getConstSpecList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangConstSpec.class);
  }

  @Override
  @NotNull
  public VlangSymbolVisibility getSymbolVisibility() {
    return notNullChild(VlangPsiTreeUtil.getChildOfType(this, VlangSymbolVisibility.class));
  }

  @Override
  @Nullable
  public PsiElement getLparen() {
    return findChildByType(LPAREN);
  }

  @Override
  @Nullable
  public PsiElement getRparen() {
    return findChildByType(RPAREN);
  }

  @Override
  @NotNull
  public PsiElement getConst() {
    return notNullChild(findChildByType(CONST));
  }

}
