// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VlangEnumDeclaration extends VlangCompositeElement {

  @Nullable
  VlangAttributes getAttributes();

  @Nullable
  VlangEnumFields getEnumFields();

  @Nullable
  VlangSymbolVisibility getSymbolVisibility();

  @Nullable
  PsiElement getLbrace();

  @Nullable
  PsiElement getRbrace();

  @NotNull
  PsiElement getEnum();

  @Nullable
  PsiElement getIdentifier();

}
