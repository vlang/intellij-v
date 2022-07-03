// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.*;
import org.vlang.lang.stubs.VlangMethodDeclarationStub;

import static org.vlang.lang.VlangTypes.FN;
import static org.vlang.lang.VlangTypes.IDENTIFIER;

public class VlangMethodDeclarationImpl extends VlangFunctionOrMethodDeclarationImpl<VlangMethodDeclarationStub> implements VlangMethodDeclaration {

  public VlangMethodDeclarationImpl(@NotNull VlangMethodDeclarationStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangMethodDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitMethodDeclaration(this);
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
  @Nullable
  public VlangBlock getBlock() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangBlock.class);
  }

  @Override
  @NotNull
  public VlangReceiver getReceiver() {
    return notNullChild(VlangPsiTreeUtil.getChildOfType(this, VlangReceiver.class));
  }

  @Override
  @Nullable
  public VlangSignature getSignature() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangSignature.class);
  }

  @Override
  @NotNull
  public VlangSymbolVisibility getSymbolVisibility() {
    return notNullChild(VlangPsiTreeUtil.getChildOfType(this, VlangSymbolVisibility.class));
  }

  @Override
  @NotNull
  public PsiElement getFn() {
    return notNullChild(findChildByType(FN));
  }

  @Override
  @NotNull
  public PsiElement getIdentifier() {
    return notNullChild(findChildByType(IDENTIFIER));
  }

}
