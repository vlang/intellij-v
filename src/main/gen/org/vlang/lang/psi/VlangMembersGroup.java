// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangMembersGroup extends VlangCompositeElement {

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

}
