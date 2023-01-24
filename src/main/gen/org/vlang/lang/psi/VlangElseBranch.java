// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangElseBranch extends VlangCompositeElement {

  @Nullable
  VlangBlock getBlock();

  @Nullable
  VlangIfExpression getIfExpression();

  @NotNull
  PsiElement getElse();

}
