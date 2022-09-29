// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.impl.VlangReference;

public interface VlangReferenceExpression extends VlangExpression, VlangReferenceExpressionBase {

  @NotNull
  PsiElement getIdentifier();

  @NotNull
  VlangReference getReference();

  @Nullable
  VlangCompositeElement getQualifier();

  @Nullable
  PsiElement resolve();

  //WARNING: getReadWriteAccess(...) is skipped
  //matching getReadWriteAccess(VlangReferenceExpression, ...)
  //methods are not found in VlangPsiImplUtil

}
