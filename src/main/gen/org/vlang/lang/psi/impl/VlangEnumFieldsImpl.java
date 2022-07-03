// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.vlang.lang.psi.VlangEnumFieldDeclaration;
import org.vlang.lang.psi.VlangEnumFields;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import org.vlang.lang.psi.VlangVisitor;

import java.util.List;

public class VlangEnumFieldsImpl extends VlangCompositeElementImpl implements VlangEnumFields {

  public VlangEnumFieldsImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitEnumFields(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<VlangEnumFieldDeclaration> getEnumFieldDeclarationList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangEnumFieldDeclaration.class);
  }

}
