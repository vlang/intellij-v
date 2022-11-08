// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;

public interface VlangSqlExpression extends VlangExpression {

  @Nullable
  VlangExpression getExpression();

  @Nullable
  VlangSqlBlock getSqlBlock();

  @Nullable
  VlangType getType(@Nullable ResolveState context);

}
