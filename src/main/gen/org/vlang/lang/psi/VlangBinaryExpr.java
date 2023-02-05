// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangBinaryExpr extends VlangExpression {

  @NotNull
  List<VlangExpression> getExpressionList();

  @NotNull
  VlangExpression getLeft();

  @Nullable
  VlangExpression getRight();

  @Nullable
  PsiElement getOperator();

}
