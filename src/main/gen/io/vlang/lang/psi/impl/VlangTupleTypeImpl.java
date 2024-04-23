// This is a generated file. Not intended for manual editing.
package io.vlang.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import io.vlang.lang.psi.VlangPsiTreeUtil;
import static io.vlang.lang.VlangTypes.*;
import io.vlang.lang.psi.*;
import com.intellij.psi.stubs.IStubElementType;
import io.vlang.lang.stubs.VlangTypeStub;

public class VlangTupleTypeImpl extends VlangTypeImpl implements VlangTupleType {

  public VlangTupleTypeImpl(@NotNull VlangTypeStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangTupleTypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitTupleType(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public VlangTypeListNoPin getTypeListNoPin() {
    return notNullChild(VlangPsiTreeUtil.getChildOfType(this, VlangTypeListNoPin.class));
  }

  @Override
  @NotNull
  public PsiElement getLparen() {
    return notNullChild(findChildByType(LPAREN));
  }

  @Override
  @NotNull
  public PsiElement getRparen() {
    return notNullChild(findChildByType(RPAREN));
  }

}
