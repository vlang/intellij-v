// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import org.jetbrains.annotations.Nullable;

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

}
