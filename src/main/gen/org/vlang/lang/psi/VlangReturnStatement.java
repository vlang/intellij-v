// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangReturnStatement extends VlangStatement {

  @NotNull
  List<VlangExpression> getExpressionList();

  @NotNull
  PsiElement getReturn();

  @Nullable
  VlangSignatureOwner getOwner();

}
