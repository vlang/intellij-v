// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

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
