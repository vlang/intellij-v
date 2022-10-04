// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface VlangParameters extends VlangCompositeElement {

  @NotNull
  List<VlangParameterDeclaration> getParameterDeclarationList();

  @Nullable
  VlangTypeListNoPin getTypeListNoPin();

  @NotNull
  PsiElement getLparen();

  @Nullable
  PsiElement getRparen();

  @NotNull
  List<VlangParamDefinition> getParametersList();

  @NotNull
  List<Pair<VlangParamDefinition, VlangType>> getParametersListWithTypes();

}
