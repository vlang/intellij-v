// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.*;

import static org.vlang.lang.VlangTypes.RAW_STRING;

public class VlangStringLiteralImpl extends VlangExpressionImpl implements VlangStringLiteral {

  public VlangStringLiteralImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitStringLiteral(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangStringTemplate getStringTemplate() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangStringTemplate.class);
  }

  @Override
  @Nullable
  public PsiElement getRawString() {
    return findChildByType(RAW_STRING);
  }

  @Override
  public boolean isValidHost() {
    return VlangPsiImplUtil.isValidHost(this);
  }

  @Override
  @NotNull
  public VlangStringLiteral updateText(@NotNull String text) {
    return VlangPsiImplUtil.updateText(this, text);
  }

  @Override
  @NotNull
  public StringLiteralEscaper<VlangStringLiteral> createLiteralTextEscaper() {
    return VlangPsiImplUtil.createLiteralTextEscaper(this);
  }

  @Override
  @NotNull
  public String getContents() {
    return VlangPsiImplUtil.getContents(this);
  }

}
