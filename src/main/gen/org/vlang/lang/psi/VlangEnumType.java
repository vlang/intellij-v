// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangEnumType extends VlangType {

  @Nullable
  VlangEnumBackedTypeAs getEnumBackedTypeAs();

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
