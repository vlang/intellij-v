// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.vlang.lang.stubs.VlangTypeStub;

public interface VlangType extends VlangGenericArgumentsOwner, StubBasedPsiElement<VlangTypeStub> {

  @Nullable
  VlangGenericArguments getGenericArguments();

  @Nullable
  VlangType getType();

  @Nullable
  VlangTypeModifiers getTypeModifiers();

  @Nullable
  VlangTypeReferenceExpression getTypeReferenceExpression();

  @Nullable
  PsiElement getIdentifier();

  @Nullable
  VlangType getUnderlyingType();

  @NotNull
  VlangType resolveType();

}
