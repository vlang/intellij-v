// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import kotlin.Pair;

public interface VlangIndexOrSliceExpr extends VlangExpression {

  @Nullable
  VlangAnonymousStructValueExpression getAnonymousStructValueExpression();

  @Nullable
  VlangCompileTimeFieldReference getCompileTimeFieldReference();

  @Nullable
  VlangEmptySlice getEmptySlice();

  @NotNull
  List<VlangExpression> getExpressionList();

  @Nullable
  VlangOptionPropagationExpression getOptionPropagationExpression();

  @Nullable
  VlangResultPropagationExpression getResultPropagationExpression();

  @Nullable
  PsiElement getHashLbrack();

  @Nullable
  PsiElement getLbrack();

  @NotNull
  PsiElement getRbrack();

  boolean isSlice();

  @Nullable
  PsiElement getExpression();

  @Nullable
  Pair<PsiElement, PsiElement> getRange();

  @Nullable
  PsiElement getSliceStart();

  @Nullable
  PsiElement getSliceEnd();

}
