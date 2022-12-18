// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.ResolveState;
import com.intellij.psi.stubs.IStubElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.VlangGenericParameter;
import org.vlang.lang.psi.VlangVisitor;
import org.vlang.lang.psi.types.VlangTypeEx;
import org.vlang.lang.stubs.VlangGenericParameterStub;

import static org.vlang.lang.VlangTypes.IDENTIFIER;

public class VlangGenericParameterImpl extends VlangNamedElementImpl<VlangGenericParameterStub> implements VlangGenericParameter {

  public VlangGenericParameterImpl(@NotNull VlangGenericParameterStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangGenericParameterImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitGenericParameter(this);
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
  public VlangTypeEx getTypeInner(@Nullable ResolveState context) {
    return VlangPsiImplUtil.getTypeInner(this, context);
  }

}
