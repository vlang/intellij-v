// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.VlangAttribute;
import org.vlang.lang.psi.VlangAttributeExpression;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import org.vlang.lang.psi.VlangVisitor;

import java.util.List;

import static org.vlang.lang.VlangTypes.LBRACK;
import static org.vlang.lang.VlangTypes.RBRACK;

public class VlangAttributeImpl extends VlangCompositeElementImpl implements VlangAttribute {

  public VlangAttributeImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitAttribute(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<VlangAttributeExpression> getAttributeExpressionList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangAttributeExpression.class);
  }

  @Override
  @NotNull
  public PsiElement getLbrack() {
    return notNullChild(findChildByType(LBRACK));
  }

  @Override
  @Nullable
  public PsiElement getRbrack() {
    return findChildByType(RBRACK);
  }

}
