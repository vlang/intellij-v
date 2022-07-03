// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface VlangForClause extends VlangCompositeElement {

  @Nullable
  VlangExpression getExpression();

  @NotNull
  List<VlangStatement> getStatementList();

  //WARNING: processDeclarations(...) is skipped
  //matching processDeclarations(VlangForClause, ...)
  //methods are not found in VlangPsiImplUtil

}
