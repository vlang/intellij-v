// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.VlangSelectiveImportList;
import org.vlang.lang.psi.VlangVisitor;

import static org.vlang.lang.VlangTypes.LBRACE;
import static org.vlang.lang.VlangTypes.RBRACE;

public class VlangSelectiveImportListImpl extends VlangCompositeElementImpl implements VlangSelectiveImportList {

  public VlangSelectiveImportListImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitSelectiveImportList(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiElement getLbrace() {
    return notNullChild(findChildByType(LBRACE));
  }

  @Override
  @Nullable
  public PsiElement getRbrace() {
    return findChildByType(RBRACE);
  }

}
