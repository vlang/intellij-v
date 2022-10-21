// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.ResolveState;
import com.intellij.psi.stubs.IStubElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.*;
import org.vlang.lang.stubs.VlangStructDeclarationStub;

public class VlangStructDeclarationImpl extends VlangNamedElementImpl<VlangStructDeclarationStub> implements VlangStructDeclaration {

  public VlangStructDeclarationImpl(@NotNull VlangStructDeclarationStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangStructDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitStructDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangAttributes getAttributes() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangAttributes.class);
  }

  @Override
  @NotNull
  public VlangStructType getStructType() {
    return notNullChild(VlangPsiTreeUtil.getStubChildOfType(this, VlangStructType.class));
  }

  @Override
  @Nullable
  public VlangSymbolVisibility getSymbolVisibility() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangSymbolVisibility.class);
  }

  @Override
  @Nullable
  public PsiElement getIdentifier() {
    return VlangPsiImplUtil.getIdentifier(this);
  }

  @Override
  @NotNull
  public String getName() {
    return VlangPsiImplUtil.getName(this);
  }

  @Override
  @NotNull
  public VlangType getTypeInner(@Nullable ResolveState context) {
    return VlangPsiImplUtil.getTypeInner(this, context);
  }

}
