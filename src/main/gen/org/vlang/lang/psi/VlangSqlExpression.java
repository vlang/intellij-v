// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import org.vlang.lang.psi.types.VlangTypeEx;

public interface VlangSqlExpression extends VlangExpression {

  @Nullable
  VlangExpression getExpression();

  @NotNull
  VlangSqlBlock getSqlBlock();

  @Nullable
  VlangTypeEx getType(@Nullable ResolveState context);

}
