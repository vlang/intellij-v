// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface VlangFieldDeclaration extends VlangCompositeElement {

  @Nullable
  VlangAnonymousFieldDefinition getAnonymousFieldDefinition();

  @Nullable
  VlangAttribute getAttribute();

  @Nullable
  VlangDefaultFieldValue getDefaultFieldValue();

  @NotNull
  List<VlangFieldDefinition> getFieldDefinitionList();

  @Nullable
  VlangTag getTag();

  @Nullable
  VlangType getType();

  @Nullable
  PsiElement getSemicolon();

  @Nullable
  PsiElement getSemicolonSynthetic();

  @Nullable
  PsiElement getShared();

}
