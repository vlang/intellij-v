// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.VlangPackageClause;
import org.vlang.lang.psi.VlangVisitor;
import org.vlang.lang.stubs.VlangPackageClauseStub;

import static org.vlang.lang.VlangTypes.IDENTIFIER;
import static org.vlang.lang.VlangTypes.PACKAGE;

public class VlangPackageClauseImpl extends VlangStubbedElementImpl<VlangPackageClauseStub> implements VlangPackageClause {

  public VlangPackageClauseImpl(@NotNull VlangPackageClauseStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangPackageClauseImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitPackageClause(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiElement getIdentifier() {
    return findChildByType(IDENTIFIER);
  }

  @Override
  @NotNull
  public PsiElement getPackage() {
    return notNullChild(findChildByType(PACKAGE));
  }

  @Override
  @NotNull
  public String getName() {
    return VlangPsiImplUtil.getName(this);
  }

}
