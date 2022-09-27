// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.StubBasedPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.stubs.VlangEnumDeclarationStub;

public interface VlangEnumDeclaration extends VlangNamedElement, StubBasedPsiElement<VlangEnumDeclarationStub> {

  @Nullable
  VlangAttributes getAttributes();

  @NotNull
  VlangEnumType getEnumType();

  @Nullable
  VlangSymbolVisibility getSymbolVisibility();

  @NotNull
  String getName();

  @Nullable
  PsiElement getIdentifier();

  @NotNull
  VlangType getTypeInner(@Nullable ResolveState context);

}
