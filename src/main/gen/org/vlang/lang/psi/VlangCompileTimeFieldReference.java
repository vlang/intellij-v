// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangCompileTimeFieldReference extends VlangCompositeElement {

  @Nullable
  VlangAnonymousStructValueExpression getAnonymousStructValueExpression();

  @Nullable
  VlangCompileTimeFieldReference getCompileTimeFieldReference();

  @Nullable
  VlangErrorPropagationExpression getErrorPropagationExpression();

  @NotNull
  List<VlangExpression> getExpressionList();

  @Nullable
  VlangForceNoErrorPropagationExpression getForceNoErrorPropagationExpression();

  @NotNull
  PsiElement getDollar();

  @NotNull
  PsiElement getDot();

  @Nullable
  PsiElement getLparen();

  @Nullable
  PsiElement getRparen();

}
