// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.VlangAttributes;
import org.vlang.lang.psi.VlangModuleClause;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import org.vlang.lang.psi.VlangVisitor;
import org.vlang.lang.stubs.VlangModuleClauseStub;

import static org.vlang.lang.VlangTypes.*;

public class VlangModuleClauseImpl extends VlangStubbedElementImpl<VlangModuleClauseStub> implements VlangModuleClause {

  public VlangModuleClauseImpl(@NotNull VlangModuleClauseStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangModuleClauseImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitModuleClause(this);
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
  @Nullable
  public PsiElement getIdentifier() {
    return findChildByType(IDENTIFIER);
  }

  @Override
  @NotNull
  public PsiElement getModule() {
    return notNullChild(findChildByType(MODULE));
  }

  @Override
  @NotNull
  public String getName() {
    return VlangPsiImplUtil.getName(this);
  }

}
