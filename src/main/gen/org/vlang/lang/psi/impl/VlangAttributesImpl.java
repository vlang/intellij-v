// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import static org.vlang.lang.VlangTypes.*;
import org.vlang.lang.stubs.VlangAttributesStub;
import org.vlang.lang.psi.*;
import com.intellij.psi.stubs.IStubElementType;

public class VlangAttributesImpl extends VlangStubbedElementImpl<VlangAttributesStub> implements VlangAttributes {

  public VlangAttributesImpl(@NotNull VlangAttributesStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangAttributesImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitAttributes(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<VlangAttribute> getAttributeList() {
    return VlangPsiTreeUtil.getStubChildrenOfTypeAsList(this, VlangAttribute.class);
  }

}
