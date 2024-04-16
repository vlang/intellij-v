// This is a generated file. Not intended for manual editing.
package io.vlang.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import io.vlang.lang.psi.VlangPsiTreeUtil;
import static io.vlang.lang.VlangTypes.*;
import io.vlang.lang.psi.*;

public class VlangVarModifiersImpl extends VlangCompositeElementImpl implements VlangVarModifiers {

  public VlangVarModifiersImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitVarModifiers(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<VlangVarModifier> getVarModifierList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangVarModifier.class);
  }

  @Override
  public boolean isMutable() {
    return VlangPsiImplUtil.isMutable(this);
  }

}
