// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface VlangBinaryExpr extends VlangExpression {

  @NotNull
  List<VlangExpression> getExpressionList();

  @NotNull
  VlangExpression getLeft();

  @Nullable
  VlangExpression getRight();

  //WARNING: getOperator(...) is skipped
  //matching getOperator(VlangBinaryExpr, ...)
  //methods are not found in VlangPsiImplUtil

}
