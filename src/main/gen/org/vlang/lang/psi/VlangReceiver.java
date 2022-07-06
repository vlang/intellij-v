// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangReceiver extends VlangNamedElement {

  @Nullable
  VlangSymbolMutability getSymbolMutability();

  @Nullable
  VlangTypeDecl getTypeDecl();

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
