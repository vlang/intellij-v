// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.stubs.VlangParamDefinitionStub;

public interface VlangParamDefinition extends VlangNamedElement, StubBasedPsiElement<VlangParamDefinitionStub> {

  @Nullable
  VlangVarModifiers getVarModifiers();

  @NotNull
  PsiElement getIdentifier();

  //WARNING: isVariadic(...) is skipped
  //matching isVariadic(VlangParamDefinition, ...)
  //methods are not found in VlangPsiImplUtil

  @NotNull
  String getName();

}
