// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.vlang.lang.stubs.VlangFunctionDeclarationStub;
import com.intellij.psi.ResolveState;

public interface VlangFunctionDeclaration extends VlangSignatureOwner, VlangFunctionOrMethodDeclaration, VlangAttributeOwner, StubBasedPsiElement<VlangFunctionDeclarationStub> {

  @Nullable
  VlangAttributes getAttributes();

  @Nullable
  VlangBlock getBlock();

  @Nullable
  VlangGenericArguments getGenericArguments();

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

  @Nullable
  VlangType getTypeInner(@Nullable ResolveState context);

  boolean isDefinition();

}
