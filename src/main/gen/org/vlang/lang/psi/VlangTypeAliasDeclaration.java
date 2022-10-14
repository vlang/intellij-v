// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.StubBasedPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.stubs.VlangTypeAliasDeclarationStub;

public interface VlangTypeAliasDeclaration extends VlangNamedElement, StubBasedPsiElement<VlangTypeAliasDeclarationStub> {

  @Nullable
  VlangAliasType getAliasType();

  @Nullable
  VlangSymbolVisibility getSymbolVisibility();

  @NotNull
  PsiElement getType_();

  @Nullable
  PsiElement getIdentifier();

  @NotNull
  String getName();

  @Nullable
  VlangType getTypeInner(@Nullable ResolveState context);

}
