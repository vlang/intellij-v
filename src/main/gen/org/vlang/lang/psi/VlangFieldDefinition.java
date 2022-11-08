// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.vlang.lang.stubs.VlangFieldDefinitionStub;
import com.intellij.psi.ResolveState;

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
