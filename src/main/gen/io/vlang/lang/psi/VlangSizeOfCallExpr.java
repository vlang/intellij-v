// This is a generated file. Not intended for manual editing.
package io.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import io.vlang.lang.psi.impl.VlangBuiltinReference;

public interface VlangSizeOfCallExpr extends VlangExpression, VlangBuiltinCallOwner {

  @Nullable
  VlangExpression getExpression();

  @Nullable
  VlangGenericArguments getGenericArguments();

  @Nullable
  PsiElement getLparen();

  @Nullable
  PsiElement getRparen();

  @NotNull
  PsiElement getSizeof();

  @NotNull VlangBuiltinReference<@NotNull VlangSizeOfCallExpr> getReference();

}
