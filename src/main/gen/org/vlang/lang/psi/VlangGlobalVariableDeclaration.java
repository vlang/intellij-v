// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangGlobalVariableDeclaration extends VlangCompositeElement {

  @Nullable
  VlangAttributes getAttributes();

  @Nullable
  VlangExpression getExpression();

  @Nullable
  VlangTypeDecl getTypeDecl();

  @Nullable
  PsiElement getAssign();

  @NotNull
  PsiElement getBuiltinGlobal();

  @NotNull
  PsiElement getIdentifier();

}
