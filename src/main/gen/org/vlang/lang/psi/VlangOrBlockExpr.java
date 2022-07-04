// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public interface VlangOrBlockExpr extends VlangBinaryExpr {

  @NotNull
  VlangBlock getBlock();

  @NotNull
  VlangExpression getExpression();

  @NotNull
  PsiElement getOr();

}
