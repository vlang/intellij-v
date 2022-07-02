// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

public interface VlangUnaryExpr extends VlangExpression {

  @Nullable
  VlangExpression getExpression();

  @Nullable
  PsiElement getBitAnd();

  @Nullable
  PsiElement getBitXor();

  @Nullable
  PsiElement getMinus();

  @Nullable
  PsiElement getMul();

  @Nullable
  PsiElement getNot();

  @Nullable
  PsiElement getPlus();

  @Nullable
  PsiElement getSendChannel();

  //WARNING: getOperator(...) is skipped
  //matching getOperator(VlangUnaryExpr, ...)
  //methods are not found in VlangPsiImplUtil

}
