// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.VlangMemberModifier;
import org.vlang.lang.psi.VlangVisitor;

import static org.vlang.lang.VlangTypes.*;

public class VlangMemberModifierImpl extends VlangCompositeElementImpl implements VlangMemberModifier {

  public VlangMemberModifierImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitMemberModifier(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiElement getBuiltinGlobal() {
    return findChildByType(BUILTIN_GLOBAL);
  }

  @Override
  @Nullable
  public PsiElement getMut() {
    return findChildByType(MUT);
  }

  @Override
  @Nullable
  public PsiElement getPub() {
    return findChildByType(PUB);
  }

}
