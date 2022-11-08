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
  public VlangAttribute getAttribute() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangAttribute.class);
  }

  @Override
  @Nullable
  public VlangDefaultFieldValue getDefaultFieldValue() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangDefaultFieldValue.class);
  }

  @Override
  @Nullable
  public VlangEmbeddedDefinition getEmbeddedDefinition() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangEmbeddedDefinition.class);
  }

  @Override
  @Nullable
  public VlangFieldDefinition getFieldDefinition() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangFieldDefinition.class);
  }

  @Override
  @Nullable
  public VlangType getType() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangType.class);
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
