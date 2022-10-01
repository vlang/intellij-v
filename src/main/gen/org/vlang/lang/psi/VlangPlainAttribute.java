// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface VlangPlainAttribute extends VlangCompositeElement {

  @NotNull
  List<VlangStringLiteral> getStringLiteralList();

  @Nullable
  PsiElement getColon();

  @Nullable
  PsiElement getDefault();

  @Nullable
  PsiElement getInt();

  @Nullable
  PsiElement getSql();

  @Nullable
  PsiElement getString();

  @Nullable
  PsiElement getUnsafe();

}
