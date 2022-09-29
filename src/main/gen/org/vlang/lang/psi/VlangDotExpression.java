// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VlangDotExpression extends VlangExpression {

  @Nullable
  VlangErrorPropagationExpression getErrorPropagationExpression();

  @NotNull
  VlangExpression getExpression();

  @Nullable
  VlangForceNoErrorPropagationExpression getForceNoErrorPropagationExpression();

}
