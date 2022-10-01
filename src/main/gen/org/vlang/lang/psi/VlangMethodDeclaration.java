// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.stubs.VlangMethodDeclarationStub;

public interface VlangMethodDeclaration extends VlangSignatureOwner, VlangFunctionOrMethodDeclaration, StubBasedPsiElement<VlangMethodDeclarationStub> {

  @Nullable
  VlangAttributes getAttributes();

  @Nullable
  VlangBlock getBlock();

  @Nullable
  VlangGenericDeclaration getGenericDeclaration();

  @NotNull
  VlangMethodName getMethodName();

  @NotNull
  VlangReceiver getReceiver();

  @Nullable
  VlangSignature getSignature();

  @Nullable
  VlangSymbolVisibility getSymbolVisibility();

  @NotNull
  PsiElement getLparen();

  @NotNull
  PsiElement getRparen();

  @NotNull
  PsiElement getFn();

  @NotNull
  VlangType getReceiverType();

  @Nullable
  PsiElement getIdentifier();

}
