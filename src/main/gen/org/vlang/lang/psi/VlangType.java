// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.stubs.VlangTypeStub;

import java.util.List;

public interface VlangType extends VlangCompositeElement, StubBasedPsiElement<VlangTypeStub> {

  @Nullable
  VlangGenericDeclaration getGenericDeclaration();

  @Nullable
  VlangType getType();

  @NotNull
  List<VlangTypeReferenceExpression> getTypeReferenceExpressionList();

  @Nullable
  PsiElement getIdentifier();

  @Nullable
  PsiElement getUnderlyingType();

  @Nullable
  VlangType resolveType();

}
