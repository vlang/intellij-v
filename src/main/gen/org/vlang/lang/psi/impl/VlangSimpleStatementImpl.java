// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.*;

public class VlangSimpleStatementImpl extends VlangStatementImpl implements VlangSimpleStatement {

  public VlangSimpleStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitSimpleStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangLeftHandExprList getLeftHandExprList() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangLeftHandExprList.class);
  }

  @Override
  @Nullable
  public VlangShortVarDeclaration getShortVarDeclaration() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangShortVarDeclaration.class);
  }

  @Override
  @Nullable
  public VlangStatement getStatement() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangStatement.class);
  }

}
