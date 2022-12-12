// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangSqlDeleteStatement extends VlangSqlBlockStatement {

  @Nullable
  VlangReferenceExpression getReferenceExpression();

  @Nullable
  VlangSqlFromClause getSqlFromClause();

  @Nullable
  VlangSqlWhereClause getSqlWhereClause();

  @Nullable
  PsiElement getSemicolon();

  @Nullable
  PsiElement getSemicolonSynthetic();

}
