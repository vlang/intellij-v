// This is a generated file. Not intended for manual editing.
package io.vlang.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import io.vlang.lang.psi.VlangPsiTreeUtil;
import static io.vlang.lang.VlangTypes.*;
import io.vlang.lang.stubs.VlangInterfaceDeclarationStub;
import io.vlang.lang.psi.*;
import com.intellij.psi.ResolveState;
import io.vlang.lang.psi.types.VlangTypeEx;
import com.intellij.psi.stubs.IStubElementType;

public class VlangInterfaceDeclarationImpl extends VlangNamedElementImpl<VlangInterfaceDeclarationStub> implements VlangInterfaceDeclaration {

  public VlangInterfaceDeclarationImpl(@NotNull VlangInterfaceDeclarationStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangInterfaceDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitInterfaceDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangAttributes getAttributes() {
    return VlangPsiTreeUtil.getStubChildOfType(this, VlangAttributes.class);
  }

  @Override
  @NotNull
  public VlangInterfaceType getInterfaceType() {
    return notNullChild(VlangPsiTreeUtil.getStubChildOfType(this, VlangInterfaceType.class));
  }

  @Override
  @Nullable
  public VlangSymbolVisibility getSymbolVisibility() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangSymbolVisibility.class);
  }

  @Override
  public @NotNull String getName() {
    return VlangPsiImplUtil.getName(this);
  }

  @Override
  public @Nullable PsiElement getIdentifier() {
    return VlangPsiImplUtil.getIdentifier(this);
  }

  @Override
  public @NotNull VlangTypeEx getTypeInner(@Nullable ResolveState context) {
    return VlangPsiImplUtil.getTypeInner(this, context);
  }

}
