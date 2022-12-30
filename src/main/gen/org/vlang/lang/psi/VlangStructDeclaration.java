// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.vlang.lang.stubs.VlangStructDeclarationStub;
import com.intellij.psi.ResolveState;
import org.vlang.lang.psi.types.VlangTypeEx;

public interface VlangStructDeclaration extends VlangNamedElement, VlangAttributeOwner, StubBasedPsiElement<VlangStructDeclarationStub> {

  @Nullable
  VlangAttributes getAttributes();

  @NotNull
  VlangStructType getStructType();

  @Nullable
  VlangSymbolVisibility getSymbolVisibility();

  @Nullable
  PsiElement getIdentifier();

  @NotNull
  String getName();

  @NotNull
  VlangTypeEx getTypeInner(@Nullable ResolveState context);

  boolean isUnion();

  @NotNull
  String getKindName();

  boolean isAttribute();

  @Nullable
  PsiElement addField(@NotNull String name, @NotNull String type, boolean mutable);

}
