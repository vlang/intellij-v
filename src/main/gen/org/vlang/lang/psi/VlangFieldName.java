// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VlangFieldName extends VlangReferenceExpressionBase {

  @NotNull
  VlangReferenceExpression getReferenceExpression();

  @Nullable
  PsiElement getIdentifier();

  @Nullable
  VlangReferenceExpressionBase getQualifier();

}
