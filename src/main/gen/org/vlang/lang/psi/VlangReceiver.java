// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.StubBasedPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.stubs.VlangReceiverStub;

public interface VlangReceiver extends VlangNamedElement, StubBasedPsiElement<VlangReceiverStub> {

  @NotNull
  VlangType getType();

  @Nullable
  VlangVarModifiers getVarModifiers();

  @Nullable
  PsiElement getComma();

  @NotNull
  PsiElement getIdentifier();

  @NotNull
  VlangType getTypeInner(@Nullable ResolveState context);

  @Nullable
  String getName();

}
