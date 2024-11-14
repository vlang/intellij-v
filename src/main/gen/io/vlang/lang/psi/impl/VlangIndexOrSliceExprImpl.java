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
import kotlin.Pair;

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
  @NotNull
  public List<VlangExpression> getExpressionList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangExpression.class);
  }

  @Override
  @Nullable
  public VlangOptionPropagationExpression getOptionPropagationExpression() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangOptionPropagationExpression.class);
  }

  @Override
  @Nullable
  public VlangResultPropagationExpression getResultPropagationExpression() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangResultPropagationExpression.class);
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

  @Override
  public boolean isSlice() {
    return VlangPsiImplUtil.isSlice(this);
  }

  @Override
  public @Nullable PsiElement getExpression() {
    return VlangPsiImplUtil.getExpression(this);
  }

  @Override
  public @Nullable Pair<@Nullable PsiElement, @Nullable PsiElement> getRange() {
    return VlangPsiImplUtil.getRange(this);
  }

  @Override
  public @Nullable PsiElement getSliceStart() {
    return VlangPsiImplUtil.getSliceStart(this);
  }

  @Override
  public @Nullable PsiElement getSliceEnd() {
    return VlangPsiImplUtil.getSliceEnd(this);
  }

}
