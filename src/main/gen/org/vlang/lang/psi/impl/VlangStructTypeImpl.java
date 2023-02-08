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

public class VlangStructTypeImpl extends VlangTypeImpl implements VlangStructType {

  public VlangStructTypeImpl(@NotNull VlangTypeStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangStructTypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitStructType(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<VlangFieldsGroup> getFieldsGroupList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangFieldsGroup.class);
  }

  @Override
  @Nullable
  public VlangGenericParameters getGenericParameters() {
    return VlangPsiTreeUtil.getStubChildOfType(this, VlangGenericParameters.class);
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
  @Nullable
  public PsiElement getStruct() {
    return findChildByType(STRUCT);
  }

  @Override
  @Nullable
  public PsiElement getUnion() {
    return findChildByType(UNION);
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
  public List<VlangStructType> getEmbeddedStructs() {
    return VlangPsiImplUtil.getEmbeddedStructs(this);
  }

  @Override
  @NotNull
  public List<VlangEmbeddedDefinition> getEmbeddedStructList() {
    return VlangPsiImplUtil.getEmbeddedStructList(this);
  }

  @Override
  public boolean isUnion() {
    return VlangPsiImplUtil.isUnion(this);
  }

}
