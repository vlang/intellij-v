// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import static org.vlang.lang.VlangTypes.*;
import org.vlang.lang.stubs.VlangAttributeExpressionStub;
import org.vlang.lang.psi.*;
import com.intellij.psi.stubs.IStubElementType;

public class VlangAttributeExpressionImpl extends VlangStubbedElementImpl<VlangAttributeExpressionStub> implements VlangAttributeExpression {

  public VlangAttributeExpressionImpl(@NotNull VlangAttributeExpressionStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangAttributeExpressionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitAttributeExpression(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangIfAttribute getIfAttribute() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangIfAttribute.class);
  }

  @Override
  @Nullable
  public VlangPlainAttribute getPlainAttribute() {
    return VlangPsiTreeUtil.getStubChildOfType(this, VlangPlainAttribute.class);
  }

}
