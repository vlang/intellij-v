// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VlangReceiver extends VlangNamedElement {

  @Nullable
  VlangSymbolMutability getSymbolMutability();

  @Nullable
  VlangType getType();

  @Nullable
  PsiElement getComma();

  @NotNull
  PsiElement getLparen();

  @Nullable
  PsiElement getRparen();

  @Nullable
  PsiElement getIdentifier();

  //WARNING: getGoTypeInner(...) is skipped
  //matching getGoTypeInner(VlangReceiver, ...)
  //methods are not found in VlangPsiImplUtil

}
