// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.ResolveState;
import org.jetbrains.annotations.Nullable;

public interface VlangSqlExpression extends VlangExpression {

  @Nullable
  VlangExpression getExpression();

  @Nullable
  VlangSqlBlock getSqlBlock();

  @Nullable
  VlangType getType(@Nullable ResolveState context);

}
