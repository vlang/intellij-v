// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.stubs.VlangTypeStub;

public interface VlangType extends VlangCompositeElement, StubBasedPsiElement<VlangTypeStub> {

  @Nullable
  VlangGenericDeclaration getGenericDeclaration();

  @Nullable
  VlangType getType();

  @Nullable
  VlangTypeReferenceExpression getTypeReferenceExpression();

  @Nullable
  PsiElement getIdentifier();

  @Nullable
  PsiElement getUnderlyingType();

  @Nullable
  VlangType resolveType();

}
