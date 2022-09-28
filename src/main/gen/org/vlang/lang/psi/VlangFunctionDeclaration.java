// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.stubs.VlangFunctionDeclarationStub;

public interface VlangFunctionDeclaration extends VlangSignatureOwner, VlangFunctionOrMethodDeclaration, StubBasedPsiElement<VlangFunctionDeclarationStub> {

  @Nullable
  VlangAttributes getAttributes();

  @Nullable
  VlangBlock getBlock();

  @Nullable
  VlangGenericDeclaration getGenericDeclaration();

  @Nullable
  VlangSignature getSignature();

  @Nullable
  VlangSymbolVisibility getSymbolVisibility();

  @NotNull
  PsiElement getFn();

  @NotNull
  PsiElement getIdentifier();

  @NotNull
  String getName();

  boolean isDefinition();

}
