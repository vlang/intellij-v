// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangResult extends VlangCompositeElement {

  @Nullable
  VlangParameters getParameters();

  @Nullable
  VlangTypeDecl getTypeDecl();

  @Nullable
  VlangTypeListNoPin getTypeListNoPin();

  @Nullable
  PsiElement getLparen();

  @Nullable
  PsiElement getRparen();

  //WARNING: isVoid(...) is skipped
  //matching isVoid(VlangResult, ...)
  //methods are not found in VlangPsiImplUtil

}
