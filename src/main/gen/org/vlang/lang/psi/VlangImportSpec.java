// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VlangImportSpec extends VlangCompositeElement {

  @Nullable
  VlangImportAlias getImportAlias();

  @NotNull
  VlangImportPath getImportPath();

  @Nullable
  VlangSelectiveImportList getSelectiveImportList();

  @NotNull
  PsiElement getIdentifier();

  @NotNull
  String getLastPart();

  @NotNull
  PsiElement getLastPartPsi();

  @NotNull
  String getName();

  @NotNull
  String getImportedName();

}
