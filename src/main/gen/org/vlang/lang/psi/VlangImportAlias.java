// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.stubs.VlangImportAliasStub;

public interface VlangImportAlias extends VlangNamedElement, StubBasedPsiElement<VlangImportAliasStub> {

  @Nullable
  VlangImportAliasName getImportAliasName();

  @NotNull
  PsiElement getAs();

  @NotNull
  String getName();

  @Nullable
  PsiElement getIdentifier();

}
