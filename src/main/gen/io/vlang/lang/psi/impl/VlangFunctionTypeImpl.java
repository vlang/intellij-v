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

public class VlangFunctionTypeImpl extends VlangTypeImpl implements VlangFunctionType {

  public VlangFunctionTypeImpl(@NotNull VlangTypeStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangFunctionTypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitFunctionType(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangGenericParameters getGenericParameters() {
    return VlangPsiTreeUtil.getStubChildOfType(this, VlangGenericParameters.class);
  }

  @Override
  @Nullable
  public VlangSignature getSignature() {
    return VlangPsiTreeUtil.getStubChildOfType(this, VlangSignature.class);
  }

  @Override
  @NotNull
  public PsiElement getFn() {
    return notNullChild(findChildByType(FN));
  }

}
