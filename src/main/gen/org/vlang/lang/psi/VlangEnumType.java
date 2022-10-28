// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface VlangEnumType extends VlangType {

  @NotNull
  List<VlangEnumFieldDeclaration> getEnumFieldDeclarationList();

  @Nullable
  PsiElement getLbrace();

  @Nullable
  PsiElement getRbrace();

  @NotNull
  PsiElement getEnum();

  @Nullable
  PsiElement getIdentifier();

  @NotNull
  List<VlangEnumFieldDefinition> getFieldList();

}
