// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.vlang.lang.psi.VlangFieldInitialization;
import org.vlang.lang.psi.VlangFieldInitializationKeyValueList;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import org.vlang.lang.psi.VlangVisitor;

public class VlangFieldInitializationImpl extends VlangCompositeElementImpl implements VlangFieldInitialization {

  public VlangFieldInitializationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitFieldInitialization(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public VlangFieldInitializationKeyValueList getFieldInitializationKeyValueList() {
    return notNullChild(VlangPsiTreeUtil.getChildOfType(this, VlangFieldInitializationKeyValueList.class));
  }

}
