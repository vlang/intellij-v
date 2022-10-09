// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.*;

import static org.vlang.lang.VlangTypes.SEMICOLON;
import static org.vlang.lang.VlangTypes.SEMICOLON_SYNTHETIC;

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
  @Nullable
  public VlangDefaultFieldValue getDefaultFieldValue() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangDefaultFieldValue.class);
  }

  @Override
  @NotNull
  public VlangInterfaceMethodDefinition getInterfaceMethodDefinition() {
    return notNullChild(VlangPsiTreeUtil.getChildOfType(this, VlangInterfaceMethodDefinition.class));
  }

  @Override
  @Nullable
  public VlangTag getTag() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangTag.class);
  }

  @Override
  @Nullable
  public PsiElement getSemicolon() {
    return findChildByType(SEMICOLON);
  }

  @Override
  @Nullable
  public PsiElement getSemicolonSynthetic() {
    return findChildByType(SEMICOLON_SYNTHETIC);
  }

}
