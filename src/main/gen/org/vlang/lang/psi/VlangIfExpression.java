// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangIfExpression extends VlangExpression {

  @Nullable
  VlangBlock getBlock();

  @Nullable
  VlangElseStatement getElseStatement();

  @Nullable
  VlangExpression getExpression();

  @Nullable
  VlangStatement getStatement();

  @Nullable
  PsiElement getSemicolon();

  @NotNull
  PsiElement getIf();

}
