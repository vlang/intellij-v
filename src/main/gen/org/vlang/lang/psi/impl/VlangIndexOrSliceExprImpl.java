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

public class VlangIndexOrSliceExprImpl extends VlangExpressionImpl implements VlangIndexOrSliceExpr {

  public VlangIndexOrSliceExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitIndexOrSliceExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangAnonymousStructValueExpression getAnonymousStructValueExpression() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangAnonymousStructValueExpression.class);
  }

  @Override
  @Nullable
  public VlangCompileTimeFieldReference getCompileTimeFieldReference() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangCompileTimeFieldReference.class);
  }

  @Override
  @Nullable
  public VlangEmptySlice getEmptySlice() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangEmptySlice.class);
  }

  @Override
  @Nullable
  public VlangErrorPropagationExpression getErrorPropagationExpression() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangErrorPropagationExpression.class);
  }

  @Override
  @NotNull
  public List<VlangExpression> getExpressionList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangExpression.class);
  }

  @Override
  @Nullable
  public VlangForceNoErrorPropagationExpression getForceNoErrorPropagationExpression() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangForceNoErrorPropagationExpression.class);
  }

  @Override
  @Nullable
  public PsiElement getHashLbrack() {
    return findChildByType(HASH_LBRACK);
  }

  @Override
  @Nullable
  public PsiElement getLbrack() {
    return findChildByType(LBRACK);
  }

  @Override
  @NotNull
  public PsiElement getRbrack() {
    return notNullChild(findChildByType(RBRACK));
  }

}
