// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangIndexOrSliceExpr extends VlangExpression {

  @Nullable
  VlangAnonymousStructValueExpression getAnonymousStructValueExpression();

  @Nullable
  VlangCompileTimeFieldReference getCompileTimeFieldReference();

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
