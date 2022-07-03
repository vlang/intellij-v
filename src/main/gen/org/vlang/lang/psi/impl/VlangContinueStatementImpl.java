// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.VlangContinueStatement;
import org.vlang.lang.psi.VlangLabelRef;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import org.vlang.lang.psi.VlangVisitor;

import static org.vlang.lang.VlangTypes.CONTINUE;

public class VlangContinueStatementImpl extends VlangStatementImpl implements VlangContinueStatement {

  public VlangContinueStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitContinueStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangLabelRef getLabelRef() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangLabelRef.class);
  }

  @Override
  @NotNull
  public PsiElement getContinue() {
    return notNullChild(findChildByType(CONTINUE));
  }

}
