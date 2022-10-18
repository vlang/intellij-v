// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface VlangIndexOrSliceExpr extends VlangExpression {

  @Nullable
  VlangEmptySlice getEmptySlice();

  @Nullable
  VlangErrorPropagationExpression getErrorPropagationExpression();

  @NotNull
  List<VlangExpression> getExpressionList();

  @Nullable
  VlangForceNoErrorPropagationExpression getForceNoErrorPropagationExpression();

  @Nullable
  PsiElement getHashLbrack();

  @Nullable
  PsiElement getLbrack();

  @NotNull
  PsiElement getRbrack();

}
