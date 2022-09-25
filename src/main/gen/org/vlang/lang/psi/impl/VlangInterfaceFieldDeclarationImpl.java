// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.*;

import java.util.List;

public class VlangInterfaceFieldDeclarationImpl extends VlangCompositeElementImpl implements VlangInterfaceFieldDeclaration {

  public VlangInterfaceFieldDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitInterfaceFieldDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangAttribute getAttribute() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangAttribute.class);
  }

  @Override
  @NotNull
  public List<VlangFieldDefinition> getFieldDefinitionList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangFieldDefinition.class);
  }

  @Override
  @Nullable
  public VlangTag getTag() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangTag.class);
  }

  @Override
  @NotNull
  public VlangType getType() {
    return notNullChild(VlangPsiTreeUtil.getChildOfType(this, VlangType.class));
  }

}
