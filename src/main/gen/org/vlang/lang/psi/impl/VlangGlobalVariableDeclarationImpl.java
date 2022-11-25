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
  @NotNull
  public List<VlangGlobalVariableDefinition> getGlobalVariableDefinitionList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangGlobalVariableDefinition.class);
  }

  @Override
  @NotNull
  public PsiElement getBuiltinGlobal() {
    return notNullChild(findChildByType(BUILTIN_GLOBAL));
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

  @Override
  public boolean isMultiline() {
    return VlangPsiImplUtil.isMultiline(this);
  }

}
