// This is a generated file. Not intended for manual editing.
package io.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import io.vlang.lang.psi.impl.VlangBuiltinReference;

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

  @NotNull VlangBuiltinReference<@NotNull VlangOffsetOfCallExpr> getReference();

}
