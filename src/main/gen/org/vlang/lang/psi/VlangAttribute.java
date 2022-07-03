// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface VlangAttribute extends VlangCompositeElement {

  @NotNull
  List<VlangAttributeExpression> getAttributeExpressionList();

  @NotNull
  PsiElement getLbrack();

  @Nullable
  PsiElement getRbrack();

}
