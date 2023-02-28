// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangCallExpr extends VlangExpression, VlangGenericArgumentsOwner {

  @Nullable
  VlangAnonymousStructValueExpression getAnonymousStructValueExpression();

  @NotNull
  VlangArgumentList getArgumentList();

  @Nullable
  VlangCompileTimeFieldReference getCompileTimeFieldReference();

  @Nullable
  VlangExpression getExpression();

  @Nullable
  VlangGenericArguments getGenericArguments();

  @Nullable
  VlangOptionPropagationExpression getOptionPropagationExpression();

  @Nullable
  VlangResultPropagationExpression getResultPropagationExpression();

  @NotNull
  List<VlangExpression> getParameters();

  @Nullable
  PsiElement getIdentifier();

  @Nullable
  VlangReferenceExpression getQualifier();

  @Nullable
  PsiElement resolve();

  int paramIndexOf(@NotNull PsiElement pos);

}
