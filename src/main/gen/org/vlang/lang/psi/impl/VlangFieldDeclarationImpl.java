// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.*;

import java.util.List;

public class VlangFieldDeclarationImpl extends VlangCompositeElementImpl implements VlangFieldDeclaration {

  public VlangFieldDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitFieldDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangAnonymousFieldDefinition getAnonymousFieldDefinition() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangAnonymousFieldDefinition.class);
  }

  @Override
  @Nullable
  public VlangAttribute getAttribute() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangAttribute.class);
  }

  @Override
  @Nullable
  public VlangDefaultFieldValue getDefaultFieldValue() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangDefaultFieldValue.class);
  }

  @Override
  @NotNull
  public List<VlangFieldName> getFieldNameList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangFieldName.class);
  }

  @Override
  @Nullable
  public VlangTag getTag() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangTag.class);
  }

  @Override
  @Nullable
  public VlangTypeDecl getTypeDecl() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangTypeDecl.class);
  }

}
