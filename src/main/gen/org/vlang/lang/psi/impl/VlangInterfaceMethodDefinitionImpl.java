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
import org.vlang.lang.stubs.VlangInterfaceMethodDefinitionStub;

import static org.vlang.lang.VlangTypes.IDENTIFIER;

public class VlangInterfaceMethodDefinitionImpl extends VlangNamedElementImpl<VlangInterfaceMethodDefinitionStub> implements VlangInterfaceMethodDefinition {

  public VlangInterfaceMethodDefinitionImpl(@NotNull VlangInterfaceMethodDefinitionStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangInterfaceMethodDefinitionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitInterfaceMethodDefinition(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public VlangSignature getSignature() {
    return notNullChild(VlangPsiTreeUtil.getChildOfType(this, VlangSignature.class));
  }

  @Override
  @NotNull
  public PsiElement getIdentifier() {
    return notNullChild(findChildByType(IDENTIFIER));
  }

  @Override
  @Nullable
  public VlangType getTypeInner(@Nullable ResolveState context) {
    return VlangPsiImplUtil.getTypeInner(this, context);
  }

  @Override
  public boolean isPublic() {
    return VlangPsiImplUtil.isPublic(this);
  }

  @Override
  @NotNull
  public VlangNamedElement getOwner() {
    return VlangPsiImplUtil.getOwner(this);
  }

}
