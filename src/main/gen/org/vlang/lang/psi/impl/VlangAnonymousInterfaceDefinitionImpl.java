// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.vlang.lang.psi.VlangAnonymousInterfaceDefinition;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import org.vlang.lang.psi.VlangTypeDecl;
import org.vlang.lang.psi.VlangVisitor;

public class VlangAnonymousInterfaceDefinitionImpl extends VlangCompositeElementImpl implements VlangAnonymousInterfaceDefinition {

  public VlangAnonymousInterfaceDefinitionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitAnonymousInterfaceDefinition(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public VlangTypeDecl getTypeDecl() {
    return notNullChild(VlangPsiTreeUtil.getChildOfType(this, VlangTypeDecl.class));
  }

}
