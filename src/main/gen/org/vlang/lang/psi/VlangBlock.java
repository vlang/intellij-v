// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface VlangBlock extends VlangCompositeElement {

  @NotNull
  List<VlangStatement> getStatementList();

  @NotNull
  PsiElement getLbrace();

  @Nullable
  PsiElement getRbrace();

  //WARNING: processDeclarations(...) is skipped
  //matching processDeclarations(VlangBlock, ...)
  //methods are not found in VlangPsiImplUtil

}
