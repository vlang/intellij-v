// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangMembersGroup extends VlangMemberModifiersOwner {

  @NotNull
  List<VlangEmbeddedInterfaceDefinition> getEmbeddedInterfaceDefinitionList();

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

  @NotNull
  List<VlangMemberModifier> getMemberModifierList();

}
