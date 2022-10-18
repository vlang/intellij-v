// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VlangSendStatement extends VlangStatement {

  @Nullable
  VlangExpression getExpression();

  @Nullable
  VlangImportList getImportList();

  @Nullable
  VlangLabelDefinition getLabelDefinition();

  @Nullable
  VlangModuleClause getModuleClause();

  @Nullable
  VlangStatement getStatement();

  @NotNull
  PsiElement getSendChannel();

  //WARNING: getSendExpression(...) is skipped
  //matching getSendExpression(VlangSendStatement, ...)
  //methods are not found in VlangPsiImplUtil

}
