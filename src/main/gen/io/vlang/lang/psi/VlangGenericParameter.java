// This is a generated file. Not intended for manual editing.
package io.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import io.vlang.lang.stubs.VlangGenericParameterStub;
import com.intellij.psi.ResolveState;
import io.vlang.lang.psi.types.VlangTypeEx;

public interface VlangGenericParameter extends VlangNamedElement, StubBasedPsiElement<VlangGenericParameterStub> {

  @NotNull
  PsiElement getIdentifier();

  @Nullable VlangTypeEx getTypeInner(@Nullable ResolveState context);

}
