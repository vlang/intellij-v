// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import org.vlang.lang.psi.VlangSymbolMutability;
import org.vlang.lang.psi.VlangVarDefinition;
import org.vlang.lang.psi.VlangVisitor;

import static org.vlang.lang.VlangTypes.IDENTIFIER;

public class VlangVarDefinitionImpl extends VlangSimpleNamedElementImpl implements VlangVarDefinition {

  public VlangVarDefinitionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitVarDefinition(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public VlangSymbolMutability getSymbolMutability() {
    return notNullChild(VlangPsiTreeUtil.getChildOfType(this, VlangSymbolMutability.class));
  }

  @Override
  @NotNull
  public PsiElement getIdentifier() {
    return notNullChild(findChildByType(IDENTIFIER));
  }

}
