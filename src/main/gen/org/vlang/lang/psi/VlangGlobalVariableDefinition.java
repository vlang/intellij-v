// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.StubBasedPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.stubs.VlangGlobalVariableDefinitionStub;

public interface VlangGlobalVariableDefinition extends VlangNamedElement, StubBasedPsiElement<VlangGlobalVariableDefinitionStub> {

  @Nullable
  VlangExpression getExpression();

  @Nullable
  VlangType getType();

  @Nullable
  VlangVarModifiers getVarModifiers();

  @Nullable
  PsiElement getAssign();

  @NotNull
  PsiElement getIdentifier();

  //WARNING: deleteDefinition(...) is skipped
  //matching deleteDefinition(VlangGlobalVariableDefinition, ...)
  //methods are not found in VlangPsiImplUtil

  //WARNING: getName(...) is skipped
  //matching getName(VlangGlobalVariableDefinition, ...)
  //methods are not found in VlangPsiImplUtil

  @Nullable
  VlangType getTypeInner(@Nullable ResolveState context);

  //WARNING: getSymbolVisibility(...) is skipped
  //matching getSymbolVisibility(VlangGlobalVariableDefinition, ...)
  //methods are not found in VlangPsiImplUtil

}
