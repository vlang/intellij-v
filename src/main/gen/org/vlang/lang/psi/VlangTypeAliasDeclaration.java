// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.vlang.lang.stubs.VlangTypeAliasDeclarationStub;

public interface VlangTypeAliasDeclaration extends VlangNamedElement, StubBasedPsiElement<VlangTypeAliasDeclarationStub> {

  @Nullable
  VlangSymbolVisibility getSymbolVisibility();

  @Nullable
  VlangTypeUnionList getTypeUnionList();

  @Nullable
  PsiElement getAssign();

  @NotNull
  PsiElement getType_();

  @Nullable
  PsiElement getIdentifier();

  @NotNull
  String getName();

}
