// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.VlangLiteral;
import org.vlang.lang.psi.VlangVisitor;

import static org.vlang.lang.VlangTypes.*;

public class VlangLiteralImpl extends VlangExpressionImpl implements VlangLiteral {

  public VlangLiteralImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitLiteral(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiElement getBin() {
    return findChildByType(BIN);
  }

  @Override
  @Nullable
  public PsiElement getChar() {
    return findChildByType(CHAR);
  }

  @Override
  @Nullable
  public PsiElement getDecimali() {
    return findChildByType(DECIMALI);
  }

  @Override
  @Nullable
  public PsiElement getFalse() {
    return findChildByType(FALSE);
  }

  @Override
  @Nullable
  public PsiElement getFloat() {
    return findChildByType(FLOAT);
  }

  @Override
  @Nullable
  public PsiElement getFloati() {
    return findChildByType(FLOATI);
  }

  @Override
  @Nullable
  public PsiElement getHex() {
    return findChildByType(HEX);
  }

  @Override
  @Nullable
  public PsiElement getInt() {
    return findChildByType(INT);
  }

  @Override
  @Nullable
  public PsiElement getNil() {
    return findChildByType(NIL);
  }

  @Override
  @Nullable
  public PsiElement getOct() {
    return findChildByType(OCT);
  }

  @Override
  @Nullable
  public PsiElement getTrue() {
    return findChildByType(TRUE);
  }

}
