// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangCompileTimeIfExpression extends VlangExpression {

  @Nullable
  VlangBlock getBlock();

  @Nullable
  VlangCompileElseStatement getCompileElseStatement();

  @Nullable
  VlangExpression getExpression();

  @Nullable
  VlangStatement getStatement();

  @NotNull
  PsiElement getIfCompileTime();

  @Nullable
  PsiElement getQuestion();

  @Nullable
  PsiElement getSemicolon();

}
