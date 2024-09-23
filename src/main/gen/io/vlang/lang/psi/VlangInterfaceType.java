// This is a generated file. Not intended for manual editing.
package io.vlang.lang.psi;

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

  @NotNull List<@NotNull VlangFieldDefinition> getFieldList();

  @NotNull List<@NotNull VlangFieldDefinition> getOwnFieldList();

  @NotNull List<@NotNull VlangInterfaceMethodDefinition> getMethodList();

  @NotNull List<@NotNull VlangInterfaceType> getEmbeddedInterfaces();

  @NotNull List<@NotNull VlangEmbeddedDefinition> getEmbeddedInterfacesList();

}
