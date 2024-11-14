// This is a generated file. Not intended for manual editing.
package io.vlang.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import io.vlang.lang.psi.VlangPsiTreeUtil;
import static io.vlang.lang.VlangTypes.*;
import io.vlang.lang.stubs.VlangImportNameStub;
import io.vlang.lang.psi.*;
import io.vlang.lang.psi.impl.imports.VlangModuleReference;
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
  public @NotNull String getQualifier() {
    return VlangPsiImplUtil.getQualifier(this);
  }

  @Override
  public @NotNull PsiElement getNameIdentifier() {
    return VlangPsiImplUtil.getNameIdentifier(this);
  }

  @Override
  public @NotNull PsiElement setName(@NotNull String newName) {
    return VlangPsiImplUtil.setName(this, newName);
  }

  @Override
  public @NotNull String getName() {
    return VlangPsiImplUtil.getName(this);
  }

  @Override
  public int getTextOffset() {
    return VlangPsiImplUtil.getTextOffset(this);
  }

  @Override
  public @NotNull VlangModuleReference<@NotNull VlangImportName> getReference() {
    return VlangPsiImplUtil.getReference(this);
  }

  @Override
  public @NotNull List<@NotNull VlangModule> resolve() {
    return VlangPsiImplUtil.resolve(this);
  }

  @Override
  public @NotNull String getQualifiedName() {
    return VlangPsiImplUtil.getQualifiedName(this);
  }

}
