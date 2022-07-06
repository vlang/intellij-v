// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangSelectiveImportList extends VlangCompositeElement {

  @NotNull
  List<VlangReferenceExpression> getReferenceExpressionList();

  @NotNull
  PsiElement getLbrace();

  @Nullable
  PsiElement getRbrace();

}
