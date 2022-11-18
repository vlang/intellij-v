// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.vlang.lang.stubs.VlangParamDefinitionStub;

public interface VlangParamDefinition extends VlangMutabilityOwner, VlangNamedElement, StubBasedPsiElement<VlangParamDefinitionStub> {

  @NotNull
  VlangType getType();

  @Nullable
  VlangVarModifiers getVarModifiers();

  @Nullable
  PsiElement getTripleDot();

  @Nullable
  PsiElement getIdentifier();

  boolean isVariadic();

  @Nullable
  String getName();

  boolean isPublic();

  boolean isMutable();

  void makeMutable();

  void makeImmutable();

}
