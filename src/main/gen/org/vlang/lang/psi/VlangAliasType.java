// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VlangAliasType extends VlangType {

  @NotNull
  VlangType getType();

  @Nullable
  VlangTypeUnionList getTypeUnionList();

  @NotNull
  PsiElement getAssign();

  @Nullable
  PsiElement getIdentifier();

  boolean isAlias();

}
