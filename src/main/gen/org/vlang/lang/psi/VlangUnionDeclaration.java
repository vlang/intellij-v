// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.stubs.VlangUnionDeclarationStub;

public interface VlangUnionDeclaration extends VlangNamedElement, StubBasedPsiElement<VlangUnionDeclarationStub> {

  @Nullable
  VlangAttributes getAttributes();

  @Nullable
  VlangSymbolVisibility getSymbolVisibility();

  @NotNull
  VlangUnionType getUnionType();

  @NotNull
  String getName();

  @Nullable
  PsiElement getIdentifier();

}
