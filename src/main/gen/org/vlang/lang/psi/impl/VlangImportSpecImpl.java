// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import static org.vlang.lang.VlangTypes.*;
import org.vlang.lang.psi.*;

public class VlangImportSpecImpl extends VlangCompositeElementImpl implements VlangImportSpec {

  public VlangImportSpecImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitImportSpec(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangImportAlias getImportAlias() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangImportAlias.class);
  }

  @Override
  @NotNull
  public VlangImportPath getImportPath() {
    return notNullChild(VlangPsiTreeUtil.getChildOfType(this, VlangImportPath.class));
  }

  @Override
  @Nullable
  public VlangSelectiveImportList getSelectiveImportList() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangSelectiveImportList.class);
  }

  @Override
  @NotNull
  public PsiElement getIdentifier() {
    return VlangPsiImplUtil.getIdentifier(this);
  }

  @Override
  @NotNull
  public String getLastPart() {
    return VlangPsiImplUtil.getLastPart(this);
  }

  @Override
  @NotNull
  public PsiElement getLastPartPsi() {
    return VlangPsiImplUtil.getLastPartPsi(this);
  }

  @Override
  @NotNull
  public String getName() {
    return VlangPsiImplUtil.getName(this);
  }

  @Override
  @NotNull
  public String getImportedName() {
    return VlangPsiImplUtil.getImportedName(this);
  }

  @Override
  @Nullable
  public String getAliasName() {
    return VlangPsiImplUtil.getAliasName(this);
  }

  @Override
  @NotNull
  public String getPathName() {
    return VlangPsiImplUtil.getPathName(this);
  }

  @Override
  @NotNull
  public List<VlangModule> resolve() {
    return VlangPsiImplUtil.resolve(this);
  }

}
