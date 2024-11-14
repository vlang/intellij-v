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

public class VlangImplementsClauseImpl extends VlangCompositeElementImpl implements VlangImplementsClause {

  public VlangImplementsClauseImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitImplementsClause(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangLastComma getLastComma() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangLastComma.class);
  }

  @Override
  @NotNull
  public List<VlangTypeReferenceExpression> getTypeReferenceExpressionList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangTypeReferenceExpression.class);
  }

  @Override
  @NotNull
  public PsiElement getImplements() {
    return notNullChild(findChildByType(IMPLEMENTS));
  }

}
