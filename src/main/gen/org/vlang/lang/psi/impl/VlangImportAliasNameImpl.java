// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.vlang.lang.psi.VlangImportAliasName;
import org.vlang.lang.psi.VlangVisitor;
import org.vlang.lang.psi.impl.imports.VlangImportReference;

import static org.vlang.lang.VlangTypes.IDENTIFIER;

public class VlangImportAliasNameImpl extends VlangCompositeElementImpl implements VlangImportAliasName {

  public VlangImportAliasNameImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitImportAliasName(this);
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
  public VlangImportReference<VlangImportAliasName> getReference() {
    return VlangPsiImplUtil.getReference(this);
  }

}
