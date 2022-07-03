// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VlangMatchExpression extends VlangExpression {

  @Nullable
  VlangExpression getExpression();

  @Nullable
  VlangMatchArms getMatchArms();

  @Nullable
  PsiElement getLbrace();

  @Nullable
  PsiElement getRbrace();

  @NotNull
  PsiElement getMatch();

}
