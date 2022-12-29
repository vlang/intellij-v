// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import static org.vlang.lang.VlangTypes.*;
import org.vlang.lang.stubs.VlangReceiverStub;
import org.vlang.lang.psi.*;
import com.intellij.psi.ResolveState;
import org.vlang.lang.psi.types.VlangTypeEx;
import com.intellij.psi.stubs.IStubElementType;

public class VlangReceiverImpl extends VlangNamedElementImpl<VlangReceiverStub> implements VlangReceiver {

  public VlangReceiverImpl(@NotNull VlangReceiverStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangReceiverImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitReceiver(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangType getType() {
    return VlangPsiTreeUtil.getStubChildOfType(this, VlangType.class);
  }

  @Override
  @Nullable
  public VlangVarModifiers getVarModifiers() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangVarModifiers.class);
  }

  @Override
  @Nullable
  public PsiElement getComma() {
    return findChildByType(COMMA);
  }

  @Override
  @NotNull
  public PsiElement getIdentifier() {
    return notNullChild(findChildByType(IDENTIFIER));
  }

  @Override
  @NotNull
  public VlangTypeEx getTypeInner(@Nullable ResolveState context) {
    return VlangPsiImplUtil.getTypeInner(this, context);
  }

  @Override
  @Nullable
  public String getName() {
    return VlangPsiImplUtil.getName(this);
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
  public void makeImmutable() {
    VlangPsiImplUtil.makeImmutable(this);
  }

  @Override
  public boolean takeReference() {
    return VlangPsiImplUtil.takeReference(this);
  }

}
