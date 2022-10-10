// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveState;
import com.intellij.psi.StubBasedPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.stubs.VlangVarDefinitionStub;

public interface VlangVarDefinition extends VlangNamedElement, StubBasedPsiElement<VlangVarDefinitionStub> {

  @Nullable
  VlangVarModifiers getVarModifiers();

  @NotNull
  PsiElement getIdentifier();

  @Nullable
  VlangType getTypeInner(@Nullable ResolveState context);

  @NotNull
  String getName();

  @Nullable
  PsiReference getReference();

  boolean isMutable();

  void makeMutable();

  //WARNING: getValue(...) is skipped
  //matching getValue(VlangVarDefinition, ...)
  //methods are not found in VlangPsiImplUtil

  @Nullable
  VlangSymbolVisibility getSymbolVisibility();

}
