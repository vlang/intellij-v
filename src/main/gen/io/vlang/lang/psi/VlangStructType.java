// This is a generated file. Not intended for manual editing.
package io.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangStructType extends VlangType, VlangFieldListOwner, VlangGenericParametersOwner {

  @NotNull
  List<VlangFieldsGroup> getFieldsGroupList();

  @Nullable
  VlangGenericParameters getGenericParameters();

  @Nullable
  VlangImplementsClause getImplementsClause();

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

  @NotNull List<@NotNull VlangFieldDefinition> getFieldList();

  @NotNull List<@NotNull VlangFieldDefinition> getOwnFieldList();

  @NotNull List<@NotNull VlangStructType> getEmbeddedStructs();

  @NotNull List<@NotNull VlangEmbeddedDefinition> getEmbeddedStructList();

  boolean isUnion();

}
