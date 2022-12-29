// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.vlang.lang.stubs.VlangReceiverStub;
import com.intellij.psi.ResolveState;
import org.vlang.lang.psi.types.VlangTypeEx;

public interface VlangReceiver extends VlangMutabilityOwner, VlangNamedElement, StubBasedPsiElement<VlangReceiverStub> {

  @Nullable
  VlangType getType();

  @Nullable
  VlangVarModifiers getVarModifiers();

  @Nullable
  PsiElement getComma();

  @NotNull
  PsiElement getIdentifier();

  @NotNull
  VlangTypeEx getTypeInner(@Nullable ResolveState context);

  @Nullable
  String getName();

  boolean isPublic();

  boolean isMutable();

  void makeMutable();

  void makeImmutable();

  boolean takeReference();

}
