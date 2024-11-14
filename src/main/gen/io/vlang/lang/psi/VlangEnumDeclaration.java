// This is a generated file. Not intended for manual editing.
package io.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import io.vlang.lang.stubs.VlangEnumDeclarationStub;
import com.intellij.psi.ResolveState;
import io.vlang.lang.psi.types.VlangTypeEx;

public interface VlangEnumDeclaration extends VlangNamedElement, VlangAttributeOwner, StubBasedPsiElement<VlangEnumDeclarationStub> {

  @Nullable
  VlangAttributes getAttributes();

  @NotNull
  VlangEnumType getEnumType();

  @Nullable
  VlangSymbolVisibility getSymbolVisibility();

  @NotNull String getName();

  @Nullable PsiElement getIdentifier();

  @NotNull VlangTypeEx getTypeInner(@Nullable ResolveState context);

  boolean isFlag();

}
