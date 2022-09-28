// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.impl.VlangReference;

public interface VlangDotExpression extends VlangExpression, VlangReferenceExpression, VlangReferenceExpressionBase {

  @NotNull
  VlangExpression getExpression();

  @Nullable
  VlangFieldLookup getFieldLookup();

  @Nullable
  VlangMethodCall getMethodCall();

  @NotNull
  PsiElement getDot();

  @Nullable
  PsiElement getIdentifier();

  @Nullable
  VlangReferenceExpression getQualifier();

  @Nullable
  PsiElement resolve();

  @NotNull
  VlangReference getReference();

}
