// This is a generated file. Not intended for manual editing.
package io.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import io.vlang.lang.stubs.VlangEmbeddedDefinitionStub;
import com.intellij.psi.ResolveState;
import io.vlang.lang.psi.types.VlangTypeEx;

public interface VlangEmbeddedDefinition extends VlangNamedElement, StubBasedPsiElement<VlangEmbeddedDefinitionStub> {

  @NotNull
  VlangType getType();

  @NotNull String getName();

  @Nullable PsiElement getIdentifier();

  @NotNull VlangTypeEx getType(@Nullable ResolveState context);

}
