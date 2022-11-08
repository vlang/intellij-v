// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangOffsetOfCallExpr extends VlangExpression {

  @Nullable
  VlangReferenceExpression getReferenceExpression();

  @Nullable
  VlangType getType();

  @Nullable
  PsiElement getComma();

  @Nullable
  PsiElement getLparen();

  @Nullable
  PsiElement getRparen();

  @NotNull
  PsiElement getOffsetof();

}
