// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangConstDeclaration extends VlangAttributeOwner {

  @Nullable
  VlangAttributes getAttributes();

  @NotNull
  List<VlangConstDefinition> getConstDefinitionList();

  @Nullable
  VlangSymbolVisibility getSymbolVisibility();

  @Nullable
  PsiElement getLparen();

  @Nullable
  PsiElement getRparen();

  @NotNull
  PsiElement getConst();

  boolean isMultiline();

}
