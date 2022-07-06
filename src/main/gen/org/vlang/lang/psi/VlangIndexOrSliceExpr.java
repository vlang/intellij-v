// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangIndexOrSliceExpr extends VlangExpression {

  @NotNull
  List<VlangExpression> getExpressionList();

  @Nullable
  PsiElement getHashLbrack();

  @Nullable
  PsiElement getLbrack();

  @Nullable
  PsiElement getRbrack();

  //WARNING: getExpression(...) is skipped
  //matching getExpression(VlangIndexOrSliceExpr, ...)
  //methods are not found in VlangPsiImplUtil

  //WARNING: getIndices(...) is skipped
  //matching getIndices(VlangIndexOrSliceExpr, ...)
  //methods are not found in VlangPsiImplUtil

}
