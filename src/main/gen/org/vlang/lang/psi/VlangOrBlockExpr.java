// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangOrBlockExpr extends VlangBinaryExpr {

  @Nullable
  VlangAnonymousStructValueExpression getAnonymousStructValueExpression();

  @NotNull
  VlangBlock getBlock();

  @Nullable
  VlangCompileTimeFieldReference getCompileTimeFieldReference();

  @Nullable
  VlangExpression getExpression();

  @Nullable
  VlangOptionPropagationExpression getOptionPropagationExpression();

  @Nullable
  VlangResultPropagationExpression getResultPropagationExpression();

  @NotNull
  PsiElement getOr();

}
