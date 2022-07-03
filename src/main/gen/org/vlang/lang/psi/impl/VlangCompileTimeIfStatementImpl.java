// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.vlang.lang.psi.VlangCompileTimeIfExpression;
import org.vlang.lang.psi.VlangCompileTimeIfStatement;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import org.vlang.lang.psi.VlangVisitor;

public class VlangCompileTimeIfStatementImpl extends VlangStatementImpl implements VlangCompileTimeIfStatement {

  public VlangCompileTimeIfStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitCompileTimeIfStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public VlangCompileTimeIfExpression getCompileTimeIfExpression() {
    return notNullChild(VlangPsiTreeUtil.getChildOfType(this, VlangCompileTimeIfExpression.class));
  }

}
