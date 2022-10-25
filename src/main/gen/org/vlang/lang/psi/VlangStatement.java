// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import org.jetbrains.annotations.Nullable;

public interface VlangStatement extends VlangCompositeElement {

  @Nullable
  VlangBlock getBlock();

  @Nullable
  VlangConstDeclaration getConstDeclaration();

  @Nullable
  VlangSqlStatement getSqlStatement();

  @Nullable
  VlangTypeAliasDeclaration getTypeAliasDeclaration();

  //WARNING: processDeclarations(...) is skipped
  //matching processDeclarations(VlangStatement, ...)
  //methods are not found in VlangPsiImplUtil

}
