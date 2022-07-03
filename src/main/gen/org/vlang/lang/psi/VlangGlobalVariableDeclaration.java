// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VlangGlobalVariableDeclaration extends VlangCompositeElement {

  @Nullable
  VlangAttributes getAttributes();

  @Nullable
  VlangExpression getExpression();

  @Nullable
  PsiElement getAssign();

  @NotNull
  PsiElement getBuiltinGlobal();

  @NotNull
  PsiElement getIdentifier();

}
