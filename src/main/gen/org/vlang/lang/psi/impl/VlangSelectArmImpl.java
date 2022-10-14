// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.*;

import static org.vlang.lang.VlangTypes.SEMICOLON;
import static org.vlang.lang.VlangTypes.SEMICOLON_SYNTHETIC;

public class VlangSelectArmImpl extends VlangCompositeElementImpl implements VlangSelectArm {

  public VlangSelectArmImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitSelectArm(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangBlock getBlock() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangBlock.class);
  }

  @Override
  @NotNull
  public VlangStatement getStatement() {
    return notNullChild(VlangPsiTreeUtil.getChildOfType(this, VlangStatement.class));
  }

  @Override
  @Nullable
  public PsiElement getSemicolon() {
    return findChildByType(SEMICOLON);
  }

  @Override
  @Nullable
  public PsiElement getSemicolonSynthetic() {
    return findChildByType(SEMICOLON_SYNTHETIC);
  }

}
