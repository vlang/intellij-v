// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import org.vlang.lang.psi.impl.VlangBuiltinReference;

public interface VlangDumpCallExpr extends VlangExpression, VlangBuiltinCallOwner {

  @Nullable
  VlangExpression getExpression();

  @Nullable
  PsiElement getLparen();

  @Nullable
  PsiElement getRparen();

  @NotNull
  PsiElement getDump();

  @NotNull
  VlangBuiltinReference<VlangDumpCallExpr> getReference();

}
