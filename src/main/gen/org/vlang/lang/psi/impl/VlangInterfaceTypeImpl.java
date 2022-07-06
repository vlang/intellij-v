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

public class VlangInterfaceTypeImpl extends VlangTypeDeclImpl implements VlangInterfaceType {

  public VlangInterfaceTypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitInterfaceType(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangAnonymousInterfaceDefinition getAnonymousInterfaceDefinition() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangAnonymousInterfaceDefinition.class);
  }

  @Override
  @Nullable
  public VlangInterfaceFieldDeclaration getInterfaceFieldDeclaration() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangInterfaceFieldDeclaration.class);
  }

  @Override
  @Nullable
  public VlangInterfaceMethodDeclaration getInterfaceMethodDeclaration() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangInterfaceMethodDeclaration.class);
  }

  @Override
  @Nullable
  public VlangMemberModifiers getMemberModifiers() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangMemberModifiers.class);
  }

  @Override
  @Nullable
  public PsiElement getLbrace() {
    return findChildByType(LBRACE);
  }

  @Override
  @Nullable
  public PsiElement getRbrace() {
    return findChildByType(RBRACE);
  }

  @Override
  @Nullable
  public PsiElement getIdentifier() {
    return findChildByType(IDENTIFIER);
  }

  @Override
  @NotNull
  public PsiElement getInterface() {
    return notNullChild(findChildByType(INTERFACE));
  }

}
