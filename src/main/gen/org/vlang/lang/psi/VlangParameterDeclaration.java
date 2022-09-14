// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangParameterDeclaration extends VlangCompositeElement {

  @NotNull
  List<VlangParamDefinition> getParamDefinitionList();

  @NotNull
  VlangType getType();

  @Nullable
  PsiElement getTripleDot();

  //WARNING: isVariadic(...) is skipped
  //matching isVariadic(VlangParameterDeclaration, ...)
  //methods are not found in VlangPsiImplUtil

}
