// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.*;

import static org.vlang.lang.VlangTypes.IDENTIFIER;

public class VlangInterfaceMethodDeclarationImpl extends VlangCompositeElementImpl implements VlangInterfaceMethodDeclaration {

  public VlangInterfaceMethodDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitInterfaceMethodDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangAttribute getAttribute() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangAttribute.class);
  }

  @Override
  @NotNull
  public VlangSignature getSignature() {
    return notNullChild(VlangPsiTreeUtil.getChildOfType(this, VlangSignature.class));
  }

  @Override
  @Nullable
  public VlangTag getTag() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangTag.class);
  }

  @Override
  @NotNull
  public PsiElement getIdentifier() {
    return notNullChild(findChildByType(IDENTIFIER));
  }

}
