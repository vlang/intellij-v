// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.*;
import org.vlang.lang.stubs.VlangFunctionDeclarationStub;

import static org.vlang.lang.VlangTypes.FN;
import static org.vlang.lang.VlangTypes.IDENTIFIER;

public class VlangFunctionDeclarationImpl extends VlangFunctionOrMethodDeclarationImpl<VlangFunctionDeclarationStub> implements VlangFunctionDeclaration {

  public VlangFunctionDeclarationImpl(@NotNull VlangFunctionDeclarationStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangFunctionDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitFunctionDeclaration(this);
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
  @Nullable
  public VlangGenericDeclaration getGenericDeclaration() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangGenericDeclaration.class);
  }

  @Override
  @Nullable
  public VlangSignature getSignature() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangSignature.class);
  }

  @Override
  @Nullable
  public VlangSymbolVisibility getSymbolVisibility() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangSymbolVisibility.class);
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

  @Override
  @NotNull
  public String getName() {
    return VlangPsiImplUtil.getName(this);
  }

  @Override
  public boolean isDefinition() {
    return VlangPsiImplUtil.isDefinition(this);
  }

}
