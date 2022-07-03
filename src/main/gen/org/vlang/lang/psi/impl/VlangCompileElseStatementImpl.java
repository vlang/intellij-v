// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.VlangCompileElseStatement;
import org.vlang.lang.psi.VlangCompileTimeIfStatement;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import org.vlang.lang.psi.VlangVisitor;

import static org.vlang.lang.VlangTypes.ELSE_COMPILE_TIME;

public class VlangCompileElseStatementImpl extends VlangStatementImpl implements VlangCompileElseStatement {

  public VlangCompileElseStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitCompileElseStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangCompileTimeIfStatement getCompileTimeIfStatement() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangCompileTimeIfStatement.class);
  }

  @Override
  @NotNull
  public PsiElement getElseCompileTime() {
    return notNullChild(findChildByType(ELSE_COMPILE_TIME));
  }

}
