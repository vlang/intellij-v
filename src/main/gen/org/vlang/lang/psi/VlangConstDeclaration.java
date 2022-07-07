// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangConstDeclaration extends VlangCompositeElement {

  @NotNull
  List<VlangConstSpec> getConstSpecList();

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
