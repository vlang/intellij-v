// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.StubBasedPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.stubs.VlangEnumFieldDefinitionStub;

public interface VlangEnumFieldDefinition extends VlangNamedElement, StubBasedPsiElement<VlangEnumFieldDefinitionStub> {

  @NotNull
  PsiElement getIdentifier();

  boolean isPublic();

  @NotNull
  VlangType getTypeInner(@Nullable ResolveState context);

}
