// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface VlangConstSpec extends VlangCompositeElement {

  @NotNull
  List<VlangConstDefinition> getConstDefinitionList();

  @NotNull
  List<VlangExpression> getExpressionList();

  @Nullable
  VlangTypeDecl getTypeDecl();

  @Nullable
  PsiElement getAssign();

  //WARNING: deleteDefinition(...) is skipped
  //matching deleteDefinition(VlangConstSpec, ...)
  //methods are not found in VlangPsiImplUtil

}
