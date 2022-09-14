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

public class VlangMapTypeImpl extends VlangTypeImpl implements VlangMapType {

  public VlangMapTypeImpl(@NotNull VlangTypeStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangMapTypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitMapType(this);
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

  @Override
  @NotNull
  public PsiElement getLbrack() {
    return notNullChild(findChildByType(LBRACK));
  }

  @Override
  @Nullable
  public PsiElement getRbrack() {
    return findChildByType(RBRACK);
  }

  @Override
  @Nullable
  public VlangType getKeyType() {
    List<VlangType> p1 = getTypeList();
    return p1.size() < 1 ? null : p1.get(0);
  }

  @Override
  @Nullable
  public VlangType getValueType() {
    List<VlangType> p1 = getTypeList();
    return p1.size() < 2 ? null : p1.get(1);
  }

}
