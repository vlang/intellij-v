// This is a generated file. Not intended for manual editing.
package io.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import io.vlang.lang.stubs.VlangGlobalVariableDefinitionStub;
import com.intellij.psi.ResolveState;
import io.vlang.lang.psi.types.VlangTypeEx;

public interface VlangGlobalVariableDefinition extends VlangNamedElement, StubBasedPsiElement<VlangGlobalVariableDefinitionStub> {

  @Nullable
  VlangExpression getExpression();

  @Nullable
  VlangType getType();

  @Nullable
  VlangVarModifiers getVarModifiers();

  @Nullable
  PsiElement getAssign();

  @Nullable
  PsiElement getIdentifier();

  //WARNING: deleteDefinition(...) is skipped
  //matching deleteDefinition(VlangGlobalVariableDefinition, ...)
  //methods are not found in VlangPsiImplUtil

  //WARNING: getName(...) is skipped
  //matching getName(VlangGlobalVariableDefinition, ...)
  //methods are not found in VlangPsiImplUtil

  @Nullable VlangTypeEx getTypeInner(@Nullable ResolveState context);

  //WARNING: getSymbolVisibility(...) is skipped
  //matching getSymbolVisibility(VlangGlobalVariableDefinition, ...)
  //methods are not found in VlangPsiImplUtil

}
