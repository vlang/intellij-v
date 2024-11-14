// This is a generated file. Not intended for manual editing.
package io.vlang.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import io.vlang.lang.psi.VlangPsiTreeUtil;
import static io.vlang.lang.VlangTypes.*;
import io.vlang.lang.stubs.VlangSignatureStub;
import io.vlang.lang.psi.*;
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
  public @NotNull Pair<@NotNull Integer, @NotNull Integer> resultCount() {
    return VlangPsiImplUtil.resultCount(this);
  }

}
