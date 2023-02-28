// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangAppendStatement extends VlangStatement {

  @NotNull
  List<VlangExpression> getExpressionList();

  @NotNull
  VlangShiftLeftOp getShiftLeftOp();

  @Nullable
  VlangExpression getLeft();

}
