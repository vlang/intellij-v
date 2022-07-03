// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VlangIfExpression extends VlangExpression {

  @Nullable
  VlangBlock getBlock();

  @Nullable
  VlangElseStatement getElseStatement();

  @Nullable
  VlangErrorPropagation getErrorPropagation();

  @Nullable
  VlangExpression getExpression();

  @Nullable
  VlangStatement getStatement();

  @Nullable
  PsiElement getSemicolon();

  @NotNull
  PsiElement getIf();

}
