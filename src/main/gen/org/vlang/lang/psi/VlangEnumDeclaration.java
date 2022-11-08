// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.vlang.lang.stubs.VlangEnumDeclarationStub;
import com.intellij.psi.ResolveState;

public interface VlangEnumDeclaration extends VlangNamedElement, VlangAttributeOwner, StubBasedPsiElement<VlangEnumDeclarationStub> {

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
