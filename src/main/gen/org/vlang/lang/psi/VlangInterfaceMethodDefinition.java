// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.vlang.lang.stubs.VlangInterfaceMethodDefinitionStub;
import com.intellij.psi.ResolveState;
import org.vlang.lang.psi.types.VlangTypeEx;

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

  @NotNull
  String getQualifiedName();

}
