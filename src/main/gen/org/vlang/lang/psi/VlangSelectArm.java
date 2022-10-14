// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VlangSelectArm extends VlangCompositeElement {

  @Nullable
  VlangBlock getBlock();

  @NotNull
  VlangStatement getStatement();

  @Nullable
  PsiElement getSemicolon();

  @Nullable
  PsiElement getSemicolonSynthetic();

}
