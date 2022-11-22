// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangAliasType extends VlangType, VlangGenericParametersOwner {

  @Nullable
  VlangGenericParameters getGenericParameters();

  @Nullable
  VlangTypeUnionList getTypeUnionList();

  @Nullable
  PsiElement getAssign();

  @NotNull
  PsiElement getIdentifier();

  boolean isAlias();

  @Nullable
  VlangType getAliasType();

}
