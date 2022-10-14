// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.VlangCapture;
import org.vlang.lang.psi.VlangCaptureList;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import org.vlang.lang.psi.VlangVisitor;

import java.util.List;

import static org.vlang.lang.VlangTypes.LBRACK;
import static org.vlang.lang.VlangTypes.RBRACK;

public class VlangCaptureListImpl extends VlangCompositeElementImpl implements VlangCaptureList {

  public VlangCaptureListImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitCaptureList(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<VlangCapture> getCaptureList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangCapture.class);
  }

  @Override
  @NotNull
  public PsiElement getLbrack() {
    return notNullChild(findChildByType(LBRACK));
  }

  @Override
  @Nullable
  public PsiElement getRbrack() {
    return findChildByType(RBRACK);
  }

  @Override
  @NotNull
  public PsiElement addCapture(@NotNull String name) {
    return VlangPsiImplUtil.addCapture(this, name);
  }

}
