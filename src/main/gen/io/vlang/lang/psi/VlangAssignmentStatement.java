// This is a generated file. Not intended for manual editing.
package io.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangAssignmentStatement extends VlangStatement {

  @NotNull
  VlangAssignOp getAssignOp();

  @NotNull
  List<VlangExpression> getExpressionList();

  @NotNull
  VlangLeftHandExprList getLeftHandExprList();

}
