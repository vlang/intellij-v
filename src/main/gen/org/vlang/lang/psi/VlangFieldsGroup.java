// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangFieldsGroup extends VlangMemberModifiersOwner {

  @NotNull
  List<VlangFieldDeclaration> getFieldDeclarationList();

  @Nullable
  VlangMemberModifiers getMemberModifiers();

  @Nullable
  VlangUnfinishedMemberModifiers getUnfinishedMemberModifiers();

  @Nullable
  PsiElement getSemicolon();

  @Nullable
  PsiElement getSemicolonSynthetic();

  @NotNull
  List<VlangMemberModifier> getMemberModifierList();

}
