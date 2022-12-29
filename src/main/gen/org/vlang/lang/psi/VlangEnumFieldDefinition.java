// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.vlang.lang.stubs.VlangEnumFieldDefinitionStub;
import com.intellij.psi.ResolveState;
import org.vlang.lang.psi.types.VlangTypeEx;

public interface VlangEnumFieldDefinition extends VlangNamedElement, StubBasedPsiElement<VlangEnumFieldDefinitionStub> {

  @NotNull
  PsiElement getIdentifier();

  boolean isPublic();

  @NotNull
  VlangTypeEx getTypeInner(@Nullable ResolveState context);

  @Nullable
  Long constantValue();

}
