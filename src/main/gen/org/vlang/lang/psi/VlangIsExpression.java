// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangIsExpression extends VlangBinaryExpr {

  @NotNull
  VlangExpression getExpression();

  @Nullable
  VlangType getType();

  @NotNull
  PsiElement getIs();

}
