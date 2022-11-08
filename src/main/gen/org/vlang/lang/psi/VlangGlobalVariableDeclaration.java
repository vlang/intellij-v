// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangGlobalVariableDeclaration extends VlangAttributeOwner {

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
