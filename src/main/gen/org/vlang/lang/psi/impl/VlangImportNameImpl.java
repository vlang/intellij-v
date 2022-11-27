// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import static org.vlang.lang.VlangTypes.*;
import org.vlang.lang.stubs.VlangImportNameStub;
import org.vlang.lang.psi.*;
import org.vlang.lang.psi.impl.imports.VlangModuleReference;
import com.intellij.psi.stubs.IStubElementType;

public class VlangImportNameImpl extends VlangNamedElementImpl<VlangImportNameStub> implements VlangImportName {

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
  @NotNull
  public String getName() {
    return VlangPsiImplUtil.getName(this);
  }

  @Override
  public int getTextOffset() {
    return VlangPsiImplUtil.getTextOffset(this);
  }

  @Override
  @NotNull
  public VlangModuleReference<VlangImportName> getReference() {
    return VlangPsiImplUtil.getReference(this);
  }

  @Override
  @NotNull
  public List<VlangModule> resolve() {
    return VlangPsiImplUtil.resolve(this);
  }

  @Override
  @NotNull
  public String getQualifiedName() {
    return VlangPsiImplUtil.getQualifiedName(this);
  }

}
