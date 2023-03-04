// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import static org.vlang.lang.VlangTypes.*;
import org.vlang.lang.stubs.VlangSignatureStub;
import org.vlang.lang.psi.*;
import kotlin.Pair;
import com.intellij.psi.stubs.IStubElementType;

public class VlangSignatureImpl extends VlangStubbedElementImpl<VlangSignatureStub> implements VlangSignature {

  public VlangSignatureImpl(@NotNull VlangSignatureStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangSignatureImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitSignature(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public VlangParameters getParameters() {
    return notNullChild(VlangPsiTreeUtil.getStubChildOfType(this, VlangParameters.class));
  }

  @Override
  @Nullable
  public VlangResult getResult() {
    return VlangPsiTreeUtil.getStubChildOfType(this, VlangResult.class);
  }

  @Override
  @NotNull
  public Pair<Integer, Integer> resultCount() {
    return VlangPsiImplUtil.resultCount(this);
  }

}
