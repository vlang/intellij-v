// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

public interface VlangResult extends VlangCompositeElement {

  @Nullable
  VlangParameters getParameters();

  @Nullable
  VlangTypeDecl getTypeDecl();

  @Nullable
  PsiElement getLparen();

  @Nullable
  PsiElement getRparen();

  //WARNING: isVoid(...) is skipped
  //matching isVoid(VlangResult, ...)
  //methods are not found in VlangPsiImplUtil

}
