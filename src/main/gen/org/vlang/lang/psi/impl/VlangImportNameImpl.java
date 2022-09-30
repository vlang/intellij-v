// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.VlangImportName;
import org.vlang.lang.psi.VlangVisitor;
import org.vlang.lang.psi.impl.imports.VlangImportReference;
import org.vlang.lang.stubs.VlangImportNameStub;

import static org.vlang.lang.VlangTypes.IDENTIFIER;

public class VlangImportNameImpl extends VlangStubbedElementImpl<VlangImportNameStub> implements VlangImportName {

  public VlangImportNameImpl(@NotNull VlangImportNameStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangImportNameImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitImportName(this);
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
  @NotNull
  public String getQualifier() {
    return VlangPsiImplUtil.getQualifier(this);
  }

  @Override
  @NotNull
  public PsiElement getNameIdentifier() {
    return VlangPsiImplUtil.getNameIdentifier(this);
  }

  @Override
  @NotNull
  public PsiElement setName(@NotNull String newName) {
    return VlangPsiImplUtil.setName(this, newName);
  }

  @Override
  @Nullable
  public String getName() {
    return VlangPsiImplUtil.getName(this);
  }

  @Override
  public int getTextOffset() {
    return VlangPsiImplUtil.getTextOffset(this);
  }

  @Override
  @NotNull
  public VlangImportReference<VlangImportName> getReference() {
    return VlangPsiImplUtil.getReference(this);
  }

  @Override
  @Nullable
  public PsiElement resolve() {
    return VlangPsiImplUtil.resolve(this);
  }

}
