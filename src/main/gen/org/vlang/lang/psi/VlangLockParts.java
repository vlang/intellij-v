// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangLockParts extends VlangCompositeElement {

  @NotNull
  List<VlangExpression> getExpressionList();

  @Nullable
  PsiElement getSemicolon();

  @Nullable
  PsiElement getLock();

  @Nullable
  PsiElement getRlock();

}
