// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.VlangImportAlias;
import org.vlang.lang.psi.VlangImportAliasName;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import org.vlang.lang.psi.VlangVisitor;
import org.vlang.lang.stubs.VlangImportAliasStub;

import static org.vlang.lang.VlangTypes.AS;

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

}
