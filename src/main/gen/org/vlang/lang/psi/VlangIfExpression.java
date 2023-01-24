// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangIfExpression extends VlangExpression {

  @Nullable
  VlangBlock getBlock();

  @Nullable
  VlangElseBranch getElseBranch();

  @Nullable
  VlangExpression getExpression();

  @Nullable
  VlangGuardVarDeclaration getGuardVarDeclaration();

  @NotNull
  PsiElement getIf();

  boolean isGuard();

}
