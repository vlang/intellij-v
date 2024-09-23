// This is a generated file. Not intended for manual editing.
package io.vlang.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import io.vlang.lang.psi.VlangPsiTreeUtil;
import static io.vlang.lang.VlangTypes.*;
import io.vlang.lang.stubs.VlangTypeStub;
import io.vlang.lang.psi.*;
import com.intellij.psi.stubs.IStubElementType;

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
  public VlangGenericArguments getGenericArguments() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangGenericArguments.class);
  }

  @Override
  @Nullable
  public VlangType getType() {
    return VlangPsiTreeUtil.getStubChildOfType(this, VlangType.class);
  }

  @Override
  @Nullable
  public VlangTypeModifiers getTypeModifiers() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangTypeModifiers.class);
  }

  @Override
  @Nullable
  public VlangTypeReferenceExpression getTypeReferenceExpression() {
    return VlangPsiTreeUtil.getStubChildOfType(this, VlangTypeReferenceExpression.class);
  }

  @Override
  public @Nullable PsiElement getIdentifier() {
    return VlangPsiImplUtil.getIdentifier(this);
  }

  @Override
  public @Nullable VlangType getUnderlyingType() {
    return VlangPsiImplUtil.getUnderlyingType(this);
  }

  @Override
  public @NotNull VlangType resolveType() {
    return VlangPsiImplUtil.resolveType(this);
  }

}
