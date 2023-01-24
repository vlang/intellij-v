// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangCompileTimeElseBranch extends VlangCompositeElement {

  @Nullable
  VlangBlock getBlock();

  @Nullable
  VlangCompileTimeIfExpression getCompileTimeIfExpression();

  @NotNull
  PsiElement getElseCompileTime();

}
