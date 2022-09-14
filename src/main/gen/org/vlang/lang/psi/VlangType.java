// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.vlang.lang.stubs.VlangTypeStub;

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

}
