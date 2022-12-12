// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangSqlReferenceListItem extends VlangCompositeElement {

  @Nullable
  VlangReferenceExpression getReferenceExpression();

  @Nullable
  VlangSqlLimitClause getSqlLimitClause();

  @Nullable
  VlangSqlOffsetClause getSqlOffsetClause();

  @Nullable
  PsiElement getSemicolon();

  @Nullable
  PsiElement getSemicolonSynthetic();

}
