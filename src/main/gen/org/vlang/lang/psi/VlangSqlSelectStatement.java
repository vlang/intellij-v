// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangSqlSelectStatement extends VlangSqlBlockStatement {

  @Nullable
  VlangSqlFromClause getSqlFromClause();

  @Nullable
  VlangSqlLimitClause getSqlLimitClause();

  @Nullable
  VlangSqlOffsetClause getSqlOffsetClause();

  @Nullable
  VlangSqlOrderByClause getSqlOrderByClause();

  @Nullable
  VlangSqlSelectCountClause getSqlSelectCountClause();

  @Nullable
  VlangSqlWhereClause getSqlWhereClause();

  @Nullable
  PsiElement getSemicolon();

  @Nullable
  PsiElement getSemicolonSynthetic();

}
