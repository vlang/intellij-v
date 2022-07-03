// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.vlang.lang.psi.VlangImportDeclaration;
import org.vlang.lang.psi.VlangImportList;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import org.vlang.lang.psi.VlangVisitor;

import java.util.List;

public class VlangImportListImpl extends VlangCompositeElementImpl implements VlangImportList {

  public VlangImportListImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitImportList(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<VlangImportDeclaration> getImportDeclarationList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangImportDeclaration.class);
  }

}
