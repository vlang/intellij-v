// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.*;
import org.vlang.lang.stubs.VlangTypeStub;

import java.util.List;

public class VlangTypeImpl extends VlangStubbedElementImpl<VlangTypeStub> implements VlangType {

  public VlangTypeImpl(@NotNull VlangTypeStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangTypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitType(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangGenericDeclaration getGenericDeclaration() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangGenericDeclaration.class);
  }

  @Override
  @Nullable
  public VlangType getType() {
    return VlangPsiTreeUtil.getStubChildOfType(this, VlangType.class);
  }

  @Override
  @NotNull
  public List<VlangTypeReferenceExpression> getTypeReferenceExpressionList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangTypeReferenceExpression.class);
  }

  @Override
  @Nullable
  public PsiElement getIdentifier() {
    return VlangPsiImplUtil.getIdentifier(this);
  }

  @Override
  @Nullable
  public PsiElement getUnderlyingType() {
    return VlangPsiImplUtil.getUnderlyingType(this);
  }

  @Override
  @Nullable
  public VlangType resolveType() {
    return VlangPsiImplUtil.resolveType(this);
  }

}
