// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangStructType extends VlangType, VlangFieldListOwner, VlangGenericParametersOwner {

  @NotNull
  List<VlangFieldsGroup> getFieldsGroupList();

  @Nullable
  VlangGenericParameters getGenericParameters();

  @Nullable
  PsiElement getLbrace();

  @Nullable
  PsiElement getRbrace();

  @Nullable
  PsiElement getIdentifier();

  @Nullable
  PsiElement getStruct();

  @Nullable
  PsiElement getUnion();

  @NotNull
  List<VlangFieldDefinition> getFieldList();

  @NotNull
  List<VlangFieldDefinition> getOwnFieldList();

  @NotNull
  List<VlangStructType> getEmbeddedStructs();

  @NotNull
  List<VlangEmbeddedDefinition> getEmbeddedStructList();

  boolean isUnion();

}
