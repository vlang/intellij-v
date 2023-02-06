// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import static org.vlang.lang.VlangTypes.*;
import org.vlang.lang.stubs.VlangPlainAttributeStub;
import org.vlang.lang.psi.*;
import com.intellij.psi.stubs.IStubElementType;

public class VlangPlainAttributeImpl extends VlangStubbedElementImpl<VlangPlainAttributeStub> implements VlangPlainAttribute {

  public VlangPlainAttributeImpl(@NotNull VlangPlainAttributeStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangPlainAttributeImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitPlainAttribute(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public VlangAttributeKey getAttributeKey() {
    return notNullChild(VlangPsiTreeUtil.getStubChildOfType(this, VlangAttributeKey.class));
  }

  @Override
  @Nullable
  public VlangAttributeValue getAttributeValue() {
    return VlangPsiTreeUtil.getStubChildOfType(this, VlangAttributeValue.class);
  }

  @Override
  @Nullable
  public PsiElement getColon() {
    return findChildByType(COLON);
  }

}
