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

public class VlangMembersGroupImpl extends VlangCompositeElementImpl implements VlangMembersGroup {

  public VlangMembersGroupImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitMembersGroup(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<VlangEmbeddedInterfaceDefinition> getEmbeddedInterfaceDefinitionList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangEmbeddedInterfaceDefinition.class);
  }

  @Override
  @NotNull
  public List<VlangFieldDeclaration> getFieldDeclarationList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangFieldDeclaration.class);
  }

  @Override
  @NotNull
  public List<VlangInterfaceMethodDeclaration> getInterfaceMethodDeclarationList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangInterfaceMethodDeclaration.class);
  }

  @Override
  @Nullable
  public VlangMemberModifiers getMemberModifiers() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangMemberModifiers.class);
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

  @Override
  @NotNull
  public List<VlangMemberModifier> getMemberModifierList() {
    return VlangPsiImplUtil.getMemberModifierList(this);
  }

}
