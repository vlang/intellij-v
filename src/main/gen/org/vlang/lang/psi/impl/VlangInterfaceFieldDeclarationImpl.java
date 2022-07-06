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
  public List<VlangFieldName> getFieldNameList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangFieldName.class);
  }

  @Override
  @Nullable
  public VlangTag getTag() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangTag.class);
  }

  @Override
  @NotNull
  public VlangTypeDecl getTypeDecl() {
    return notNullChild(VlangPsiTreeUtil.getChildOfType(this, VlangTypeDecl.class));
  }

}
