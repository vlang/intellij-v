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

public class VlangImportPathImpl extends VlangCompositeElementImpl implements VlangImportPath {

  public VlangImportPathImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitImportPath(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<VlangImportName> getImportNameList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangImportName.class);
  }

  @Override
  @NotNull
  public String getQualifiedName() {
    return VlangPsiImplUtil.getQualifiedName(this);
  }

  @Override
  @NotNull
  public String getLastPart() {
    return VlangPsiImplUtil.getLastPart(this);
  }

  @Override
  @NotNull
  public PsiElement getLastPartPsi() {
    return VlangPsiImplUtil.getLastPartPsi(this);
  }

}
