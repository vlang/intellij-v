// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangVarDeclaration extends VlangCompositeElement {

  @NotNull
  List<VlangExpression> getExpressionList();

  @NotNull
  List<VlangVarDefinition> getVarDefinitionList();

  @NotNull
  PsiElement getVarAssign();

}
