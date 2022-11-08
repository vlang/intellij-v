// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import static org.vlang.lang.VlangTypes.*;
import org.vlang.lang.psi.*;

public class VlangAssignOpImpl extends VlangCompositeElementImpl implements VlangAssignOp {

  public VlangAssignOpImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitAssignOp(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiElement getAssign() {
    return findChildByType(ASSIGN);
  }

  @Override
  @Nullable
  public PsiElement getBitAndAssign() {
    return findChildByType(BIT_AND_ASSIGN);
  }

  @Override
  @Nullable
  public PsiElement getBitClearAssign() {
    return findChildByType(BIT_CLEAR_ASSIGN);
  }

  @Override
  @Nullable
  public PsiElement getBitOrAssign() {
    return findChildByType(BIT_OR_ASSIGN);
  }

  @Override
  @Nullable
  public PsiElement getBitXorAssign() {
    return findChildByType(BIT_XOR_ASSIGN);
  }

  @Override
  @Nullable
  public PsiElement getMinusAssign() {
    return findChildByType(MINUS_ASSIGN);
  }

  @Override
  @Nullable
  public PsiElement getMulAssign() {
    return findChildByType(MUL_ASSIGN);
  }

  @Override
  @Nullable
  public PsiElement getPlusAssign() {
    return findChildByType(PLUS_ASSIGN);
  }

  @Override
  @Nullable
  public PsiElement getQuotientAssign() {
    return findChildByType(QUOTIENT_ASSIGN);
  }

  @Override
  @Nullable
  public PsiElement getRemainderAssign() {
    return findChildByType(REMAINDER_ASSIGN);
  }

  @Override
  @Nullable
  public PsiElement getShiftLeftAssign() {
    return findChildByType(SHIFT_LEFT_ASSIGN);
  }

  @Override
  @Nullable
  public PsiElement getShiftRightAssign() {
    return findChildByType(SHIFT_RIGHT_ASSIGN);
  }

  @Override
  @Nullable
  public PsiElement getUnsignedShiftRightAssign() {
    return findChildByType(UNSIGNED_SHIFT_RIGHT_ASSIGN);
  }

}
