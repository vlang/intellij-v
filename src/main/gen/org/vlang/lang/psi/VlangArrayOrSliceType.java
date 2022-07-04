// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VlangArrayOrSliceType extends VlangTypeDecl {

  @Nullable
  VlangExpression getExpression();

  @Nullable
  VlangTypeDecl getTypeDecl();

  @NotNull
  PsiElement getLbrack();

  @Nullable
  PsiElement getRbrack();

  @Nullable
  PsiElement getTripleDot();

}
