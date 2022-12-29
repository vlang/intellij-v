// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import org.vlang.lang.psi.impl.VlangBuiltinReference;

public interface VlangOffsetOfCallExpr extends VlangExpression, VlangBuiltinCallOwner {

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

  @NotNull
  VlangBuiltinReference<VlangOffsetOfCallExpr> getReference();

}
