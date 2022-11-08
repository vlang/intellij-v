// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangRangeExpr extends VlangExpression {

  @NotNull
  List<VlangExpression> getExpressionList();

  @Nullable
  PsiElement getRange();

  @Nullable
  PsiElement getTripleDot();

  @NotNull
  VlangExpression getLeft();

  @Nullable
  VlangExpression getRight();

}
