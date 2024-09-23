// This is a generated file. Not intended for manual editing.
package io.vlang.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import io.vlang.lang.psi.VlangPsiTreeUtil;
import static io.vlang.lang.VlangTypes.*;
import io.vlang.lang.psi.*;

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
  public @NotNull PsiElement getIdentifier() {
    return VlangPsiImplUtil.getIdentifier(this);
  }

  @Override
  public @NotNull String getLastPart() {
    return VlangPsiImplUtil.getLastPart(this);
  }

  @Override
  public @NotNull PsiElement getLastPartPsi() {
    return VlangPsiImplUtil.getLastPartPsi(this);
  }

  @Override
  public @NotNull String getName() {
    return VlangPsiImplUtil.getName(this);
  }

  @Override
  public @NotNull String getImportedName() {
    return VlangPsiImplUtil.getImportedName(this);
  }

  @Override
  public @Nullable String getAliasName() {
    return VlangPsiImplUtil.getAliasName(this);
  }

  @Override
  public @NotNull String getPathName() {
    return VlangPsiImplUtil.getPathName(this);
  }

  @Override
  public @NotNull List<@NotNull VlangModule> resolve() {
    return VlangPsiImplUtil.resolve(this);
  }

}
