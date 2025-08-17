// This is a generated file. Not intended for manual editing.
package io.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangEnumFieldDeclaration extends VlangCompositeElement {

  @Nullable
  VlangAttributes getAttributes();

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
