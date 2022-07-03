// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.stubs.VlangModuleClauseStub;

public interface VlangModuleClause extends VlangCompositeElement, StubBasedPsiElement<VlangModuleClauseStub> {

  @Nullable
  VlangAttributes getAttributes();

  @Nullable
  PsiElement getSemicolon();

  @Nullable
  PsiElement getSemicolonSynthetic();

  @Nullable
  PsiElement getIdentifier();

  @NotNull
  PsiElement getModule();

  @NotNull
  String getName();

}
