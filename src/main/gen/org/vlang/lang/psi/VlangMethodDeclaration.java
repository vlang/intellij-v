// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.vlang.lang.stubs.VlangMethodDeclarationStub;
import com.intellij.psi.ResolveState;

public interface VlangMethodDeclaration extends VlangSignatureOwner, VlangFunctionOrMethodDeclaration, VlangAttributeOwner, StubBasedPsiElement<VlangMethodDeclarationStub> {

  @Nullable
  VlangAttributes getAttributes();

  @Nullable
  VlangBlock getBlock();

  @Nullable
  VlangGenericArguments getGenericArguments();

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
  VlangType getTypeInner(@Nullable ResolveState context);

  @Nullable
  PsiElement getIdentifier();

}
