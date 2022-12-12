// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.vlang.lang.stubs.VlangFieldDefinitionStub;
import com.intellij.psi.ResolveState;
import org.vlang.lang.psi.types.VlangTypeEx;

public interface VlangFieldDefinition extends VlangMutabilityOwner, VlangNamedElement, StubBasedPsiElement<VlangFieldDefinitionStub> {

  @NotNull
  PsiElement getIdentifier();

  @Nullable
  VlangCompositeElement getQualifier();

  @Nullable
  String getQualifiedName();

  @NotNull
  VlangTypeEx getTypeInner(@Nullable ResolveState context);

  boolean isPublic();

  boolean isMutable();

  boolean isPrimary();

  void makeMutable();

  void makeImmutable();

  @NotNull
  VlangNamedElement getOwner();

}
