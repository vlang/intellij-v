// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.stubs.VlangPackageClauseStub;

public interface VlangPackageClause extends VlangCompositeElement, StubBasedPsiElement<VlangPackageClauseStub> {

  @Nullable
  PsiElement getIdentifier();

  @NotNull
  PsiElement getPackage();

  @NotNull
  String getName();

}
