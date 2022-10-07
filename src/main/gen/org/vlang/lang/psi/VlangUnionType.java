// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface VlangUnionType extends VlangType, VlangFieldListOwner {

  @NotNull
  List<VlangFieldsGroup> getFieldsGroupList();

  @Nullable
  PsiElement getLbrace();

  @Nullable
  PsiElement getRbrace();

  @Nullable
  PsiElement getIdentifier();

  @NotNull
  PsiElement getUnion();

  @NotNull
  List<VlangFieldDefinition> getFieldList();

}
