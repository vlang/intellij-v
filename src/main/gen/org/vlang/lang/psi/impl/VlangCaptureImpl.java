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

public class VlangCaptureImpl extends VlangCompositeElementImpl implements VlangCapture {

  public VlangCaptureImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitCapture(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public VlangReferenceExpression getReferenceExpression() {
    return notNullChild(VlangPsiTreeUtil.getChildOfType(this, VlangReferenceExpression.class));
  }

  @Override
  @Nullable
  public VlangVarModifiers getVarModifiers() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangVarModifiers.class);
  }

  @Override
  public boolean isMutable() {
    return VlangPsiImplUtil.isMutable(this);
  }

  @Override
  public void makeMutable() {
    VlangPsiImplUtil.makeMutable(this);
  }

}
