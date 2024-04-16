// This is a generated file. Not intended for manual editing.
package io.vlang.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import io.vlang.lang.psi.VlangPsiTreeUtil;
import static io.vlang.lang.VlangTypes.*;
import io.vlang.lang.stubs.VlangGenericParametersStub;
import io.vlang.lang.psi.*;
import com.intellij.psi.stubs.IStubElementType;

public class VlangGenericParametersImpl extends VlangStubbedElementImpl<VlangGenericParametersStub> implements VlangGenericParameters {

  public VlangGenericParametersImpl(@NotNull VlangGenericParametersStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangGenericParametersImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitGenericParameters(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<VlangGenericParameter> getParameters() {
    return VlangPsiImplUtil.getParameters(this);
  }

}
