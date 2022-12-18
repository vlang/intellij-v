// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.StubBasedPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.types.VlangTypeEx;
import org.vlang.lang.stubs.VlangMethodDeclarationStub;

public interface VlangMethodDeclaration extends VlangSignatureOwner, VlangFunctionOrMethodDeclaration, VlangAttributeOwner, VlangGenericParametersOwner, StubBasedPsiElement<VlangMethodDeclarationStub> {

  @Nullable
  VlangAttributes getAttributes();

  @Nullable
  VlangBlock getBlock();

  @Nullable
  VlangGenericParameters getGenericParameters();

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

  @Nullable
  VlangType getReceiverType();

  @Nullable
  VlangTypeEx getTypeInner(@Nullable ResolveState context);

  @Nullable
  PsiElement getIdentifier();

  @Nullable
  String getQualifiedName();

  @Nullable
  VlangNamedElement getOwner();

}
