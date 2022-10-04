// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface VlangCallExpr extends VlangExpression {

  @NotNull
  VlangArgumentList getArgumentList();

  @Nullable
  VlangErrorPropagationExpression getErrorPropagationExpression();

  @Nullable
  VlangExpression getExpression();

  @Nullable
  VlangForceNoErrorPropagationExpression getForceNoErrorPropagationExpression();

  @Nullable
  VlangGenericArguments getGenericArguments();

  @NotNull
  List<VlangExpression> getParameters();

}
