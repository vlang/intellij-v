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

public class VlangResultImpl extends VlangCompositeElementImpl implements VlangResult {

  public VlangResultImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitResult(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangParameters getParameters() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangParameters.class);
  }

  @Override
  @Nullable
  public VlangTypeDecl getTypeDecl() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangTypeDecl.class);
  }

  @Override
  @Nullable
  public VlangTypeListNoPin getTypeListNoPin() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangTypeListNoPin.class);
  }

  @Override
  @Nullable
  public PsiElement getLparen() {
    return findChildByType(LPAREN);
  }

  @Override
  @Nullable
  public PsiElement getRparen() {
    return findChildByType(RPAREN);
  }

}
