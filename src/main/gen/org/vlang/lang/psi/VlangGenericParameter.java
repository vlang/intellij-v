// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.StubBasedPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.types.VlangTypeEx;
import org.vlang.lang.stubs.VlangGenericParameterStub;

public interface VlangGenericParameter extends VlangNamedElement, StubBasedPsiElement<VlangGenericParameterStub> {

  @NotNull
  PsiElement getIdentifier();

  @Nullable
  VlangTypeEx getTypeInner(@Nullable ResolveState context);

}
