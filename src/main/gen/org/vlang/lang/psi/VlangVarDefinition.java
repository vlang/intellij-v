// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.vlang.lang.stubs.VlangVarDefinitionStub;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveState;
import org.vlang.lang.psi.types.VlangTypeEx;

public interface VlangVarDefinition extends VlangMutabilityOwner, VlangNamedElement, StubBasedPsiElement<VlangVarDefinitionStub> {

  @Nullable
  VlangVarModifiers getVarModifiers();

  @NotNull
  PsiElement getIdentifier();

  @Nullable
  VlangTypeEx getTypeInner(@Nullable ResolveState context);

  @NotNull
  String getName();

  @NotNull
  PsiReference getReference();

  boolean isMutable();

  boolean isPublic();

  void makeMutable();

  void makeImmutable();

  boolean isCaptured(@NotNull PsiElement original);

  //WARNING: getValue(...) is skipped
  //matching getValue(VlangVarDefinition, ...)
  //methods are not found in VlangPsiImplUtil

  @Nullable
  VlangSymbolVisibility getSymbolVisibility();

}
