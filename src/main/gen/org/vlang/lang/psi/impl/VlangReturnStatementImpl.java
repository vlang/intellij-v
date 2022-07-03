// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.vlang.lang.psi.VlangExpression;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import org.vlang.lang.psi.VlangReturnStatement;
import org.vlang.lang.psi.VlangVisitor;

import java.util.List;

import static org.vlang.lang.VlangTypes.RETURN;

public class VlangReturnStatementImpl extends VlangStatementImpl implements VlangReturnStatement {

  public VlangReturnStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitReturnStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<VlangExpression> getExpressionList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangExpression.class);
  }

  @Override
  @NotNull
  public PsiElement getReturn() {
    return notNullChild(findChildByType(RETURN));
  }

}
