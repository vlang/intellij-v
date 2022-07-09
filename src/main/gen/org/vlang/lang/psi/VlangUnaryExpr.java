// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangUnaryExpr extends VlangExpression {

  @Nullable
  VlangExpression getExpression();

  @Nullable
  PsiElement getBitAnd();

  @Nullable
  PsiElement getBitXor();

  @Nullable
  PsiElement getCondAnd();

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

  @Nullable
  PsiElement getTilda();

  //WARNING: getOperator(...) is skipped
  //matching getOperator(VlangUnaryExpr, ...)
  //methods are not found in VlangPsiImplUtil

}
