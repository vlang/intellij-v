// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

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
