// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import static org.vlang.lang.VlangTypes.*;
import org.vlang.lang.stubs.VlangStructDeclarationStub;
import org.vlang.lang.psi.*;
import com.intellij.psi.ResolveState;
import org.vlang.lang.psi.types.VlangTypeEx;
import com.intellij.psi.stubs.IStubElementType;

public class VlangStructDeclarationImpl extends VlangNamedElementImpl<VlangStructDeclarationStub> implements VlangStructDeclaration {

  public VlangStructDeclarationImpl(@NotNull VlangStructDeclarationStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangStructDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitStructDeclaration(this);
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
  public VlangStructType getStructType() {
    return notNullChild(VlangPsiTreeUtil.getStubChildOfType(this, VlangStructType.class));
  }

  @Override
  @Nullable
  public VlangSymbolVisibility getSymbolVisibility() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangSymbolVisibility.class);
  }

  @Override
  @Nullable
  public PsiElement getIdentifier() {
    return VlangPsiImplUtil.getIdentifier(this);
  }

  @Override
  @NotNull
  public String getName() {
    return VlangPsiImplUtil.getName(this);
  }

  @Override
  @NotNull
  public VlangTypeEx getTypeInner(@Nullable ResolveState context) {
    return VlangPsiImplUtil.getTypeInner(this, context);
  }

  @Override
  public boolean isUnion() {
    return VlangPsiImplUtil.isUnion(this);
  }

  @Override
  @NotNull
  public String getKindName() {
    return VlangPsiImplUtil.getKindName(this);
  }

  @Override
  public boolean isAttribute() {
    return VlangPsiImplUtil.isAttribute(this);
  }

  @Override
  @Nullable
  public PsiElement addField(@NotNull String name, @NotNull String type, boolean mutable) {
    return VlangPsiImplUtil.addField(this, name, type, mutable);
  }

}
