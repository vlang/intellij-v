// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface VlangMembersGroup extends VlangCompositeElement {

  @NotNull
  List<VlangAnonymousInterfaceDefinition> getAnonymousInterfaceDefinitionList();

  @NotNull
  List<VlangFieldDeclaration> getFieldDeclarationList();

  @NotNull
  List<VlangInterfaceMethodDeclaration> getInterfaceMethodDeclarationList();

  @Nullable
  VlangMemberModifiers getMemberModifiers();

  @Nullable
  PsiElement getSemicolon();

  @Nullable
  PsiElement getSemicolonSynthetic();

}
