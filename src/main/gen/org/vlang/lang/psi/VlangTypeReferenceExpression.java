// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.vlang.lang.stubs.VlangTypeReferenceExpressionStub;
import org.vlang.lang.psi.impl.VlangReference;

public interface VlangTypeReferenceExpression extends VlangReferenceExpressionBase, StubBasedPsiElement<VlangTypeReferenceExpressionStub> {

  @NotNull
  PsiElement getIdentifier();

  @NotNull
  VlangReference getReference();

  @Nullable
  VlangCompositeElement getQualifier();

  @Nullable
  PsiElement resolve();

  //WARNING: getType(...) is skipped
  //matching getType(VlangTypeReferenceExpression, ...)
  //methods are not found in VlangPsiImplUtil

}
