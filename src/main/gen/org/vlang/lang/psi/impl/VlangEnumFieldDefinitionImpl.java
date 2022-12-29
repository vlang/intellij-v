// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import static org.vlang.lang.VlangTypes.*;
import org.vlang.lang.stubs.VlangEnumFieldDefinitionStub;
import org.vlang.lang.psi.*;
import com.intellij.psi.ResolveState;
import org.vlang.lang.psi.types.VlangTypeEx;
import com.intellij.psi.stubs.IStubElementType;

public class VlangEnumFieldDefinitionImpl extends VlangNamedElementImpl<VlangEnumFieldDefinitionStub> implements VlangEnumFieldDefinition {

  public VlangEnumFieldDefinitionImpl(@NotNull VlangEnumFieldDefinitionStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangEnumFieldDefinitionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitEnumFieldDefinition(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiElement getIdentifier() {
    return notNullChild(findChildByType(IDENTIFIER));
  }

  @Override
  public boolean isPublic() {
    return VlangPsiImplUtil.isPublic(this);
  }

  @Override
  @NotNull
  public VlangTypeEx getTypeInner(@Nullable ResolveState context) {
    return VlangPsiImplUtil.getTypeInner(this, context);
  }

  @Override
  @Nullable
  public Long constantValue() {
    return VlangPsiImplUtil.constantValue(this);
  }

}
