// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.stubs.VlangParamDefinitionStub;

public interface VlangParamDefinition extends VlangMutable, VlangNamedElement, StubBasedPsiElement<VlangParamDefinitionStub> {

  @Nullable
  VlangVarModifiers getVarModifiers();

  @NotNull
  PsiElement getIdentifier();

  boolean isVariadic();

  @NotNull
  String getName();

  boolean isPublic();

  boolean isMutable();

  void makeMutable();

}
