// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangSqlExpression extends VlangExpression {

  @Nullable
  VlangSqlBlock getSqlBlock();

  @Nullable
  PsiElement getIdentifier();

  @NotNull
  PsiElement getSql();

}
