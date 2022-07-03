// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.VlangMemberModifier;
import org.vlang.lang.psi.VlangMemberModifiers;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import org.vlang.lang.psi.VlangVisitor;

import java.util.List;

import static org.vlang.lang.VlangTypes.COLON;

public class VlangMemberModifiersImpl extends VlangCompositeElementImpl implements VlangMemberModifiers {

  public VlangMemberModifiersImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitMemberModifiers(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<VlangMemberModifier> getMemberModifierList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangMemberModifier.class);
  }

  @Override
  @Nullable
  public PsiElement getColon() {
    return findChildByType(COLON);
  }

}
