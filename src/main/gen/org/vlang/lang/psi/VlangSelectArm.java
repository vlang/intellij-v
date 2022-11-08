// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangSelectArm extends VlangCompositeElement {

  @NotNull
  VlangBlock getBlock();

  @NotNull
  VlangStatement getStatement();

  @Nullable
  PsiElement getSemicolon();

  @Nullable
  PsiElement getSemicolonSynthetic();

}
