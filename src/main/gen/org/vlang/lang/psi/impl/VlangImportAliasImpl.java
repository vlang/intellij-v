// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import static org.vlang.lang.VlangTypes.*;
import org.vlang.lang.stubs.VlangImportAliasStub;
import org.vlang.lang.psi.*;
import com.intellij.psi.stubs.IStubElementType;

public class VlangImportAliasImpl extends VlangNamedElementImpl<VlangImportAliasStub> implements VlangImportAlias {

  public VlangImportAliasImpl(@NotNull VlangImportAliasStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangImportAliasImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitImportAlias(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangImportAliasName getImportAliasName() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangImportAliasName.class);
  }

  @Override
  @NotNull
  public PsiElement getAs() {
    return notNullChild(findChildByType(AS));
  }

  @Override
  @NotNull
  public String getName() {
    return VlangPsiImplUtil.getName(this);
  }

  @Override
  @Nullable
  public PsiElement getIdentifier() {
    return VlangPsiImplUtil.getIdentifier(this);
  }

  @Override
  @NotNull
  public String getQualifiedName() {
    return VlangPsiImplUtil.getQualifiedName(this);
  }

}
