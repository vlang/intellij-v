// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import static org.vlang.lang.VlangTypes.*;
import org.vlang.lang.stubs.VlangTypeReferenceExpressionStub;
import org.vlang.lang.psi.*;
import com.intellij.psi.stubs.IStubElementType;

public class VlangTypeReferenceExpressionImpl extends VlangStubbedElementImpl<VlangTypeReferenceExpressionStub> implements VlangTypeReferenceExpression {

  public VlangTypeReferenceExpressionImpl(@NotNull VlangTypeReferenceExpressionStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangTypeReferenceExpressionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitTypeReferenceExpression(this);
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
  @NotNull
  public VlangReference getReference() {
    return VlangPsiImplUtil.getReference(this);
  }

  @Override
  @Nullable
  public VlangCompositeElement getQualifier() {
    return VlangPsiImplUtil.getQualifier(this);
  }

  @Override
  @Nullable
  public PsiElement resolve() {
    return VlangPsiImplUtil.resolve(this);
  }

}
