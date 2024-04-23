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

public class VlangArrayTypeImpl extends VlangTypeImpl implements VlangArrayType {

  public VlangArrayTypeImpl(@NotNull VlangTypeStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangArrayTypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitArrayType(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public VlangType getType() {
    return notNullChild(VlangPsiTreeUtil.getStubChildOfType(this, VlangType.class));
  }

  @Override
  @NotNull
  public PsiElement getLbrack() {
    return notNullChild(findChildByType(LBRACK));
  }

  @Override
  @NotNull
  public PsiElement getRbrack() {
    return notNullChild(findChildByType(RBRACK));
  }

}
