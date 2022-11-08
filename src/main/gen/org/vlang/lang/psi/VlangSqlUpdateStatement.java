// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangSqlUpdateStatement extends VlangSqlBlockStatement {

  @Nullable
  VlangSqlTableName getSqlTableName();

  @Nullable
  VlangSqlUpdateList getSqlUpdateList();

  @Nullable
  VlangSqlWhereClause getSqlWhereClause();

}
