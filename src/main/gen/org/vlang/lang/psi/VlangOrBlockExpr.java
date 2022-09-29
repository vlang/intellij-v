// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VlangOrBlockExpr extends VlangBinaryExpr {

  @NotNull
  VlangBlock getBlock();

  @Nullable
  VlangErrorPropagationExpression getErrorPropagationExpression();

  @Nullable
  VlangExpression getExpression();

  @Nullable
  VlangForceNoErrorPropagationExpression getForceNoErrorPropagationExpression();

  @NotNull
  PsiElement getOr();

}
