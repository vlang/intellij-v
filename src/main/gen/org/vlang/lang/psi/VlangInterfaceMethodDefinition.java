// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.StubBasedPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.types.VlangTypeEx;
import org.vlang.lang.stubs.VlangInterfaceMethodDefinitionStub;

public interface VlangInterfaceMethodDefinition extends VlangSignatureOwner, VlangNamedElement, VlangGenericParametersOwner, StubBasedPsiElement<VlangInterfaceMethodDefinitionStub> {

  @Nullable
  VlangGenericParameters getGenericParameters();

  @NotNull
  VlangSignature getSignature();

  @NotNull
  PsiElement getIdentifier();

  @Nullable
  VlangTypeEx getTypeInner(@Nullable ResolveState context);

  boolean isPublic();

  boolean isMutable();

  @NotNull
  VlangInterfaceDeclaration getOwner();

  @Nullable
  String getQualifiedName();

}
