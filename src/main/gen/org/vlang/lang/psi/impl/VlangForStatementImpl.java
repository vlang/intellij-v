// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.*;

import static org.vlang.lang.VlangTypes.FOR;

public class VlangForStatementImpl extends VlangStatementImpl implements VlangForStatement {

  public VlangForStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitForStatement(this);
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
  @Nullable
  public VlangExpression getExpression() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangExpression.class);
  }

  @Override
  @Nullable
  public VlangForClause getForClause() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangForClause.class);
  }

  @Override
  @Nullable
  public VlangRangeClause getRangeClause() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangRangeClause.class);
  }

  @Override
  @NotNull
  public PsiElement getFor() {
    return notNullChild(findChildByType(FOR));
  }

}
