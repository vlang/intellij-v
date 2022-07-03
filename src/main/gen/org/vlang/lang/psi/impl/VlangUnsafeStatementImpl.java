// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import org.vlang.lang.psi.VlangUnsafeExpression;
import org.vlang.lang.psi.VlangUnsafeStatement;
import org.vlang.lang.psi.VlangVisitor;

public class VlangUnsafeStatementImpl extends VlangStatementImpl implements VlangUnsafeStatement {

  public VlangUnsafeStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitUnsafeStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public VlangUnsafeExpression getUnsafeExpression() {
    return notNullChild(VlangPsiTreeUtil.getChildOfType(this, VlangUnsafeExpression.class));
  }

}
