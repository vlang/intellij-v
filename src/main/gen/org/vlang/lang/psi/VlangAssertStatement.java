// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface VlangAssertStatement extends VlangStatement {

  @NotNull
  List<VlangExpression> getExpressionList();

  @Nullable
  PsiElement getComma();

  @NotNull
  PsiElement getAssert();

}
