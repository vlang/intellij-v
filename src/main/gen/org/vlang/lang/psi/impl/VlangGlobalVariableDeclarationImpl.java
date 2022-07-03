// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.*;

import static org.vlang.lang.VlangTypes.*;

public class VlangGlobalVariableDeclarationImpl extends VlangCompositeElementImpl implements VlangGlobalVariableDeclaration {

  public VlangGlobalVariableDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitGlobalVariableDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangAttributes getAttributes() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangAttributes.class);
  }

  @Override
  @Nullable
  public VlangExpression getExpression() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangExpression.class);
  }

  @Override
  @Nullable
  public PsiElement getAssign() {
    return findChildByType(ASSIGN);
  }

  @Override
  @NotNull
  public PsiElement getBuiltinGlobal() {
    return notNullChild(findChildByType(BUILTIN_GLOBAL));
  }

  @Override
  @NotNull
  public PsiElement getIdentifier() {
    return notNullChild(findChildByType(IDENTIFIER));
  }

}
