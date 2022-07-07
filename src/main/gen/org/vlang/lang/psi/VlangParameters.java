// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangParameters extends VlangCompositeElement {

  @NotNull
  List<VlangParameterDeclaration> getParameterDeclarationList();

  @Nullable
  VlangTypeListNoPin getTypeListNoPin();

  @NotNull
  PsiElement getLparen();

  @Nullable
  PsiElement getRparen();

}
