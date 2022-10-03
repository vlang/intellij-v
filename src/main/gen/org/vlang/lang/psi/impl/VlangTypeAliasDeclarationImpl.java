// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.*;
import org.vlang.lang.stubs.VlangTypeAliasDeclarationStub;

import static org.vlang.lang.VlangTypes.*;

public class VlangTypeAliasDeclarationImpl extends VlangNamedElementImpl<VlangTypeAliasDeclarationStub> implements VlangTypeAliasDeclaration {

  public VlangTypeAliasDeclarationImpl(@NotNull VlangTypeAliasDeclarationStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangTypeAliasDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitTypeAliasDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangGenericArguments getGenericArguments() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangGenericArguments.class);
  }

  @Override
  @Nullable
  public VlangSymbolVisibility getSymbolVisibility() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangSymbolVisibility.class);
  }

  @Override
  @Nullable
  public VlangTypeUnionList getTypeUnionList() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangTypeUnionList.class);
  }

  @Override
  @Nullable
  public PsiElement getAssign() {
    return findChildByType(ASSIGN);
  }

  @Override
  @NotNull
  public PsiElement getType_() {
    return notNullChild(findChildByType(TYPE_));
  }

  @Override
  @Nullable
  public PsiElement getIdentifier() {
    return findChildByType(IDENTIFIER);
  }

  @Override
  @NotNull
  public String getName() {
    return VlangPsiImplUtil.getName(this);
  }

}
