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

public class VlangCIncludeStatementImpl extends VlangStatementImpl implements VlangCIncludeStatement {

  public VlangCIncludeStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitCIncludeStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangStringLiteral getStringLiteral() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangStringLiteral.class);
  }

  @Override
  @NotNull
  public PsiElement getCInclude() {
    return notNullChild(findChildByType(C_INCLUDE));
  }

}
