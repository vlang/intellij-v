// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import static org.vlang.lang.VlangTypes.*;
import org.vlang.lang.stubs.VlangTypeUnionListStub;
import org.vlang.lang.psi.*;
import com.intellij.psi.stubs.IStubElementType;

public class VlangTypeUnionListImpl extends VlangStubbedElementImpl<VlangTypeUnionListStub> implements VlangTypeUnionList {

  public VlangTypeUnionListImpl(@NotNull VlangTypeUnionListStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangTypeUnionListImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitTypeUnionList(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<VlangType> getTypeList() {
    return VlangPsiTreeUtil.getStubChildrenOfTypeAsList(this, VlangType.class);
  }

}
