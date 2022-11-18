// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import static org.vlang.lang.VlangTypes.*;
import org.vlang.lang.stubs.VlangParamDefinitionStub;
import org.vlang.lang.psi.*;
import com.intellij.psi.stubs.IStubElementType;

public class VlangParamDefinitionImpl extends VlangNamedElementImpl<VlangParamDefinitionStub> implements VlangParamDefinition {

  public VlangParamDefinitionImpl(@NotNull VlangParamDefinitionStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangParamDefinitionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitParamDefinition(this);
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
  @Nullable
  public VlangVarModifiers getVarModifiers() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangVarModifiers.class);
  }

  @Override
  @Nullable
  public PsiElement getTripleDot() {
    return findChildByType(TRIPLE_DOT);
  }

  @Override
  @Nullable
  public PsiElement getIdentifier() {
    return findChildByType(IDENTIFIER);
  }

  @Override
  public boolean isVariadic() {
    return VlangPsiImplUtil.isVariadic(this);
  }

  @Override
  @Nullable
  public String getName() {
    return VlangPsiImplUtil.getName(this);
  }

  @Override
  public boolean isPublic() {
    return VlangPsiImplUtil.isPublic(this);
  }

  @Override
  public boolean isMutable() {
    return VlangPsiImplUtil.isMutable(this);
  }

  @Override
  public void makeMutable() {
    VlangPsiImplUtil.makeMutable(this);
  }

  @Override
  public void makeImmutable() {
    VlangPsiImplUtil.makeImmutable(this);
  }

}
