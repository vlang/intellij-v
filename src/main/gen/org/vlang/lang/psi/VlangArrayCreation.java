// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangArrayCreation extends VlangExpression {

  @Nullable
  VlangArrayCreationList getArrayCreationList();

  @NotNull
  PsiElement getLbrack();

  @Nullable
  PsiElement getNot();

  @NotNull
  PsiElement getRbrack();

  @Nullable
  PsiElement getSemicolon();

  @Nullable
  PsiElement getSemicolonSynthetic();

  boolean isFixedSize();

}
