// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.VlangCompositeElement;
import org.vlang.lang.psi.VlangFieldDefinition;
import org.vlang.lang.psi.VlangNamedElement;
import org.vlang.lang.psi.VlangVisitor;
import org.vlang.lang.stubs.VlangFieldDefinitionStub;

import static org.vlang.lang.VlangTypes.IDENTIFIER;

public class VlangFieldDefinitionImpl extends VlangNamedElementImpl<VlangFieldDefinitionStub> implements VlangFieldDefinition {

  public VlangFieldDefinitionImpl(@NotNull VlangFieldDefinitionStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangFieldDefinitionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitFieldDefinition(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiElement getIdentifier() {
    return notNullChild(findChildByType(IDENTIFIER));
  }

  @Override
  @Nullable
  public VlangCompositeElement getQualifier() {
    return VlangPsiImplUtil.getQualifier(this);
  }

  @Override
  @Nullable
  public String getQualifiedName() {
    return VlangPsiImplUtil.getQualifiedName(this);
  }

  @Override
  public boolean isPublic() {
    return VlangPsiImplUtil.isPublic(this);
  }

  @Override
  public boolean isMutable() {
    return VlangPsiImplUtil.isMutable(this);
  }

  @Override
  public void makeMutable() {
    VlangPsiImplUtil.makeMutable(this);
  }

  @Override
  @NotNull
  public VlangNamedElement getOwner() {
    return VlangPsiImplUtil.getOwner(this);
  }

}
