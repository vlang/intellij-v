// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import static org.vlang.lang.VlangTypes.*;
import org.vlang.lang.psi.*;
import com.intellij.psi.stubs.IStubElementType;
import org.vlang.lang.stubs.VlangTypeStub;

public class VlangInterfaceTypeImpl extends VlangTypeImpl implements VlangInterfaceType {

  public VlangInterfaceTypeImpl(@NotNull VlangTypeStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangInterfaceTypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitInterfaceType(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangGenericParameters getGenericParameters() {
    return VlangPsiTreeUtil.getStubChildOfType(this, VlangGenericParameters.class);
  }

  @Override
  @NotNull
  public List<VlangMembersGroup> getMembersGroupList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangMembersGroup.class);
  }

  @Override
  @Nullable
  public PsiElement getLbrace() {
    return findChildByType(LBRACE);
  }

  @Override
  @Nullable
  public PsiElement getRbrace() {
    return findChildByType(RBRACE);
  }

  @Override
  @Nullable
  public PsiElement getIdentifier() {
    return findChildByType(IDENTIFIER);
  }

  @Override
  @NotNull
  public PsiElement getInterface() {
    return notNullChild(findChildByType(INTERFACE));
  }

  @Override
  @NotNull
  public List<VlangFieldDefinition> getFieldList() {
    return VlangPsiImplUtil.getFieldList(this);
  }

  @Override
  @NotNull
  public List<VlangFieldDefinition> getOwnFieldList() {
    return VlangPsiImplUtil.getOwnFieldList(this);
  }

  @Override
  @NotNull
  public List<VlangInterfaceMethodDefinition> getMethodList() {
    return VlangPsiImplUtil.getMethodList(this);
  }

  @Override
  @NotNull
  public List<VlangInterfaceType> getEmbeddedInterfaces() {
    return VlangPsiImplUtil.getEmbeddedInterfaces(this);
  }

  @Override
  @NotNull
  public List<VlangEmbeddedDefinition> getEmbeddedInterfacesList() {
    return VlangPsiImplUtil.getEmbeddedInterfacesList(this);
  }

}
