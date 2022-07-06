// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangAttribute extends VlangCompositeElement {

  @NotNull
  List<VlangAttributeExpression> getAttributeExpressionList();

  @NotNull
  PsiElement getLbrack();

  @Nullable
  PsiElement getRbrack();

}
