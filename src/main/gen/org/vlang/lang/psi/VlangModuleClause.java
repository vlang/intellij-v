// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.vlang.lang.stubs.VlangModuleClauseStub;

public interface VlangModuleClause extends VlangNamedElement, VlangAttributeOwner, StubBasedPsiElement<VlangModuleClauseStub> {

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
