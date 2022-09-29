// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.*;
import org.vlang.lang.psi.impl.imports.VlangImportReference;
import org.vlang.lang.stubs.VlangImportPathStub;

import java.util.List;

public class VlangImportPathImpl extends VlangNamedElementImpl<VlangImportPathStub> implements VlangImportPath {

  public VlangImportPathImpl(@NotNull VlangImportPathStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangImportPathImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitImportPath(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<VlangImportName> getImportNameList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangImportName.class);
  }

  @Override
  @Nullable
  public PsiElement getIdentifier() {
    return VlangPsiImplUtil.getIdentifier(this);
  }

  @Override
  @Nullable
  public VlangCompositeElement getQualifier() {
    return VlangPsiImplUtil.getQualifier(this);
  }

  @Override
  @NotNull
  public VlangImportReference<VlangImportPath> getReference() {
    return VlangPsiImplUtil.getReference(this);
  }

}
