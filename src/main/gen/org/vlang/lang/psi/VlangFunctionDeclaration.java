// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.vlang.lang.stubs.VlangFunctionDeclarationStub;
import com.intellij.psi.ResolveState;
import org.vlang.lang.psi.types.VlangTypeEx;

public interface VlangFunctionDeclaration extends VlangSignatureOwner, VlangFunctionOrMethodDeclaration, VlangAttributeOwner, VlangGenericParametersOwner, VlangScopeHolder, StubBasedPsiElement<VlangFunctionDeclarationStub> {

  @Nullable
  VlangAttributes getAttributes();

  @Nullable
  VlangBlock getBlock();

  @Nullable
  VlangGenericParameters getGenericParameters();

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
  VlangTypeEx getTypeInner(@Nullable ResolveState context);

  boolean isDefinition();

  boolean isNoReturn();

  boolean isGeneric();

  boolean isCompileTime();

}
