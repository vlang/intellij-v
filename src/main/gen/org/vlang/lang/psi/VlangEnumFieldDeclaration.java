// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VlangEnumFieldDeclaration extends VlangCompositeElement {

  @NotNull
  VlangEnumFieldDefinition getEnumFieldDefinition();

  @Nullable
  VlangExpression getExpression();

  @Nullable
  PsiElement getAssign();

  @Nullable
  PsiElement getSemicolon();

  @Nullable
  PsiElement getSemicolonSynthetic();

}
