// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VlangArrayCreation extends VlangExpression {

  @Nullable
  VlangArrayCreationList getArrayCreationList();

  @NotNull
  PsiElement getLbrack();

  @Nullable
  PsiElement getNot();

  @Nullable
  PsiElement getRbrack();

  @Nullable
  PsiElement getSemicolon();

  @Nullable
  PsiElement getSemicolonSynthetic();

}
