// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import org.jetbrains.annotations.Nullable;

public interface VlangSqlUpdateStatement extends VlangSqlBlockStatement {

  @Nullable
  VlangSqlTableName getSqlTableName();

  @Nullable
  VlangSqlUpdateList getSqlUpdateList();

  @Nullable
  VlangSqlWhereClause getSqlWhereClause();

}
