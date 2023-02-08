// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangInterfaceType extends VlangType, VlangFieldListOwner, VlangGenericParametersOwner {

  @Nullable
  VlangGenericParameters getGenericParameters();

  @NotNull
  List<VlangMembersGroup> getMembersGroupList();

  @Nullable
  PsiElement getLbrace();

  @Nullable
  PsiElement getRbrace();

  @Nullable
  PsiElement getIdentifier();

  @NotNull
  PsiElement getInterface();

  @NotNull
  List<VlangFieldDefinition> getFieldList();

  @NotNull
  List<VlangFieldDefinition> getOwnFieldList();

  @NotNull
  List<VlangInterfaceMethodDefinition> getMethodList();

  @NotNull
  List<VlangInterfaceType> getEmbeddedInterfaces();

  @NotNull
  List<VlangEmbeddedDefinition> getEmbeddedInterfacesList();

}
