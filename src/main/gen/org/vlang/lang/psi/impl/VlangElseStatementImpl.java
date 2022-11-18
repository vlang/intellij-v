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

public class VlangElseStatementImpl extends VlangStatementImpl implements VlangElseStatement {

  public VlangElseStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitElseStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangIfStatement getIfStatement() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangIfStatement.class);
  }

  @Override
  @NotNull
  public PsiElement getElse() {
    return notNullChild(findChildByType(ELSE));
  }

  @Override
  @Nullable
  public VlangBlock getBlock() {
    return VlangPsiImplUtil.getBlock(this);
  }

}
