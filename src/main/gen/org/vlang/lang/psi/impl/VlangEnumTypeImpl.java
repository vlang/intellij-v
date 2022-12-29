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
import com.intellij.psi.stubs.IStubElementType;
import org.vlang.lang.stubs.VlangTypeStub;

public class VlangEnumTypeImpl extends VlangTypeImpl implements VlangEnumType {

  public VlangEnumTypeImpl(@NotNull VlangTypeStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangEnumTypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitEnumType(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangEnumBackedTypeAs getEnumBackedTypeAs() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangEnumBackedTypeAs.class);
  }

  @Override
  @NotNull
  public List<VlangEnumFieldDeclaration> getEnumFieldDeclarationList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangEnumFieldDeclaration.class);
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
  @NotNull
  public PsiElement getEnum() {
    return notNullChild(findChildByType(ENUM));
  }

  @Override
  @Nullable
  public PsiElement getIdentifier() {
    return findChildByType(IDENTIFIER);
  }

  @Override
  @NotNull
  public List<VlangEnumFieldDefinition> getFieldList() {
    return VlangPsiImplUtil.getFieldList(this);
  }

}
