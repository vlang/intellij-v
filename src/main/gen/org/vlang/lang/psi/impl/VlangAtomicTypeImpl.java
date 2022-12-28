// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import org.jetbrains.annotations.NotNull;
import org.vlang.lang.psi.VlangAtomicType;
import org.vlang.lang.psi.VlangVisitor;
import org.vlang.lang.stubs.VlangTypeStub;

import static org.vlang.lang.VlangTypes.ATOMIC;

public class VlangAtomicTypeImpl extends VlangTypeImpl implements VlangAtomicType {

  public VlangAtomicTypeImpl(@NotNull VlangTypeStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangAtomicTypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitAtomicType(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiElement getAtomic() {
    return notNullChild(findChildByType(ATOMIC));
  }

}
