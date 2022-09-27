// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.stubs.VlangInterfaceDeclarationStub;

public interface VlangInterfaceDeclaration extends VlangNamedElement, StubBasedPsiElement<VlangInterfaceDeclarationStub> {

  @Nullable
  VlangAttributes getAttributes();

  @NotNull
  VlangInterfaceType getInterfaceType();

  @Nullable
  VlangSymbolVisibility getSymbolVisibility();

  @NotNull
  String getName();

  @Nullable
  PsiElement getIdentifier();

}
