// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface VlangMatchArm extends VlangCompositeElement {

  @Nullable
  VlangBlock getBlock();

  @NotNull
  List<VlangExpression> getExpressionList();

  @NotNull
  List<VlangType> getTypeList();

  @Nullable
  PsiElement getSemicolon();

  @Nullable
  PsiElement getSemicolonSynthetic();

}
