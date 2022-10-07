// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VlangCompileTimeIfExpression extends VlangExpression {

  @Nullable
  VlangBlock getBlock();

  @Nullable
  VlangCompileElseStatement getCompileElseStatement();

  @Nullable
  VlangExpression getExpression();

  @Nullable
  VlangVarDeclaration getVarDeclaration();

  @NotNull
  PsiElement getIfCompileTime();

}
