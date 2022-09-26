// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface VlangConstDeclaration extends VlangCompositeElement {

  @NotNull
  List<VlangConstDefinition> getConstDefinitionList();

  @Nullable
  VlangSymbolVisibility getSymbolVisibility();

  @Nullable
  PsiElement getLparen();

  @Nullable
  PsiElement getRparen();

  @NotNull
  PsiElement getConst();

  //WARNING: addSpec(...) is skipped
  //matching addSpec(VlangConstDeclaration, ...)
  //methods are not found in VlangPsiImplUtil

  //WARNING: deleteSpec(...) is skipped
  //matching deleteSpec(VlangConstDeclaration, ...)
  //methods are not found in VlangPsiImplUtil

}
