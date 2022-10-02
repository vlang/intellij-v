// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface VlangGlobalVariableDeclaration extends VlangCompositeElement {

  @Nullable
  VlangAttributes getAttributes();

  @NotNull
  List<VlangGlobalVariableDefinition> getGlobalVariableDefinitionList();

  @NotNull
  PsiElement getBuiltinGlobal();

  @Nullable
  PsiElement getLparen();

  @Nullable
  PsiElement getRparen();

  //WARNING: addSpec(...) is skipped
  //matching addSpec(VlangGlobalVariableDeclaration, ...)
  //methods are not found in VlangPsiImplUtil

  //WARNING: deleteSpec(...) is skipped
  //matching deleteSpec(VlangGlobalVariableDeclaration, ...)
  //methods are not found in VlangPsiImplUtil

}
