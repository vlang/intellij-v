// This is a generated file. Not intended for manual editing.
package io.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import io.vlang.lang.stubs.VlangMethodDeclarationStub;
import com.intellij.psi.ResolveState;
import io.vlang.lang.psi.types.VlangTypeEx;

public interface VlangMethodDeclaration extends VlangSignatureOwner, VlangFunctionOrMethodDeclaration, VlangAttributeOwner, VlangGenericParametersOwner, VlangScopeHolder, StubBasedPsiElement<VlangMethodDeclarationStub> {

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

  @NotNull
  VlangSignature getSignature();

  @Nullable
  VlangSymbolVisibility getSymbolVisibility();

  @NotNull
  PsiElement getLparen();

  @NotNull
  PsiElement getRparen();

  @NotNull
  PsiElement getFn();

  @Nullable VlangType getReceiverType();

  @Nullable VlangTypeEx getTypeInner(@Nullable ResolveState context);

  @Nullable PsiElement getIdentifier();

  @Nullable String getQualifiedName();

  @Nullable VlangNamedElement getOwner();

  boolean isMutable();

  boolean byReference();

}
