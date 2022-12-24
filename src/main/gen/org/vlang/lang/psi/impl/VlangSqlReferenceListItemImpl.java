// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import org.vlang.lang.psi.VlangReferenceExpression;
import org.vlang.lang.psi.VlangSqlReferenceListItem;
import org.vlang.lang.psi.VlangVisitor;

import static org.vlang.lang.VlangTypes.SEMICOLON;
import static org.vlang.lang.VlangTypes.SEMICOLON_SYNTHETIC;

public class VlangSqlReferenceListItemImpl extends VlangCompositeElementImpl implements VlangSqlReferenceListItem {

  public VlangSqlReferenceListItemImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitSqlReferenceListItem(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public VlangReferenceExpression getReferenceExpression() {
    return notNullChild(VlangPsiTreeUtil.getChildOfType(this, VlangReferenceExpression.class));
  }

  @Override
  @Nullable
  public PsiElement getSemicolon() {
    return findChildByType(SEMICOLON);
  }

  @Override
  @Nullable
  public PsiElement getSemicolonSynthetic() {
    return findChildByType(SEMICOLON_SYNTHETIC);
  }

}
