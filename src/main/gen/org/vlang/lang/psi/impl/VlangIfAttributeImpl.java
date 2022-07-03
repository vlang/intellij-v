// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.VlangIfAttribute;
import org.vlang.lang.psi.VlangVisitor;

import static org.vlang.lang.VlangTypes.IDENTIFIER;
import static org.vlang.lang.VlangTypes.IF;

public class VlangIfAttributeImpl extends VlangCompositeElementImpl implements VlangIfAttribute {

  public VlangIfAttributeImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitIfAttribute(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiElement getIdentifier() {
    return findChildByType(IDENTIFIER);
  }

  @Override
  @NotNull
  public PsiElement getIf() {
    return notNullChild(findChildByType(IF));
  }

}
