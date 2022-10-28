// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.StubBasedPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.stubs.VlangConstDefinitionStub;

public interface VlangConstDefinition extends VlangNamedElement, StubBasedPsiElement<VlangConstDefinitionStub> {

  @Nullable
  VlangExpression getExpression();

  @NotNull
  PsiElement getAssign();

  @NotNull
  PsiElement getIdentifier();

  @Nullable
  VlangType getTypeInner(@Nullable ResolveState context);

  boolean isPublic();

  //WARNING: deleteDefinition(...) is skipped
  //matching deleteDefinition(VlangConstDefinition, ...)
  //methods are not found in VlangPsiImplUtil

  @NotNull
  String getName();

  @Nullable
  VlangSymbolVisibility getSymbolVisibility();

}
