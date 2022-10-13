// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.vlang.lang.psi.VlangForceNoErrorPropagationExpression;
import org.vlang.lang.psi.VlangVisitor;

import static org.vlang.lang.VlangTypes.NOT;

public class VlangForceNoErrorPropagationExpressionImpl extends VlangCompositeElementImpl implements VlangForceNoErrorPropagationExpression {

  public VlangForceNoErrorPropagationExpressionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitForceNoErrorPropagationExpression(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiElement getNot() {
    return notNullChild(findChildByType(NOT));
  }

}