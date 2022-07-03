// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.VlangPlainAttribute;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import org.vlang.lang.psi.VlangStringLiteral;
import org.vlang.lang.psi.VlangVisitor;

import static org.vlang.lang.VlangTypes.*;

public class VlangPlainAttributeImpl extends VlangCompositeElementImpl implements VlangPlainAttribute {

  public VlangPlainAttributeImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitPlainAttribute(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangStringLiteral getStringLiteral() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangStringLiteral.class);
  }

  @Override
  @Nullable
  public PsiElement getColon() {
    return findChildByType(COLON);
  }

  @Override
  @Nullable
  public PsiElement getInt() {
    return findChildByType(INT);
  }

  @Override
  @Nullable
  public PsiElement getString() {
    return findChildByType(STRING);
  }

  @Override
  @Nullable
  public PsiElement getUnsafe() {
    return findChildByType(UNSAFE);
  }

}
