// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

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
  VlangSelectArm getSelectArm();

  @Nullable
  VlangSelectElseArmClause getSelectElseArmClause();

  @Nullable
  VlangShebangClause getShebangClause();

  @Nullable
  VlangStatement getStatement();

  @NotNull
  PsiElement getSendChannel();

}
