// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface VlangKeyValue extends VlangCompositeElement {

  @NotNull
  List<VlangExpression> getExpressionList();

  @NotNull
  PsiElement getColon();

  @NotNull
  VlangExpression getKeyExpr();

  @Nullable
  VlangExpression getValueExpr();

}
