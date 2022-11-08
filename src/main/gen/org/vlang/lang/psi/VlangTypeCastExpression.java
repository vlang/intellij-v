// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangTypeCastExpression extends VlangExpression {

  @Nullable
  VlangExpression getExpression();

  @NotNull
  VlangType getType();

  @NotNull
  PsiElement getLparen();

  @Nullable
  PsiElement getRparen();

}
