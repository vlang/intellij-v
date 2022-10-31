// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.StubBasedPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.stubs.VlangFieldDefinitionStub;

public interface VlangFieldDefinition extends VlangMutable, VlangNamedElement, StubBasedPsiElement<VlangFieldDefinitionStub> {

  @NotNull
  PsiElement getIdentifier();

  @Nullable
  VlangCompositeElement getQualifier();

  @Nullable
  String getQualifiedName();

  @Nullable
  VlangType getTypeInner(@Nullable ResolveState context);

  boolean isPublic();

  boolean isMutable();

  void makeMutable();

  @NotNull
  VlangNamedElement getOwner();

}
