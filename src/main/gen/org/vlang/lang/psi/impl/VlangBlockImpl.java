// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.VlangBlock;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import org.vlang.lang.psi.VlangStatement;
import org.vlang.lang.psi.VlangVisitor;

import java.util.List;

import static org.vlang.lang.VlangTypes.LBRACE;
import static org.vlang.lang.VlangTypes.RBRACE;

public class VlangBlockImpl extends VlangCompositeElementImpl implements VlangBlock {

  public VlangBlockImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitBlock(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<VlangStatement> getStatementList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangStatement.class);
  }

  @Override
  @NotNull
  public PsiElement getLbrace() {
    return notNullChild(findChildByType(LBRACE));
  }

  @Override
  @Nullable
  public PsiElement getRbrace() {
    return findChildByType(RBRACE);
  }

}
