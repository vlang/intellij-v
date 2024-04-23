// This is a generated file. Not intended for manual editing.
package io.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import io.vlang.lang.stubs.VlangConstDefinitionStub;
import com.intellij.psi.ResolveState;
import io.vlang.lang.psi.types.VlangTypeEx;

public interface VlangConstDefinition extends VlangNamedElement, StubBasedPsiElement<VlangConstDefinitionStub> {

  @Nullable
  VlangExpression getExpression();

  @NotNull
  PsiElement getAssign();

  @NotNull
  PsiElement getIdentifier();

  @Nullable
  VlangTypeEx getTypeInner(@Nullable ResolveState context);

  boolean isPublic();

  boolean isCompileTime();

  //WARNING: deleteDefinition(...) is skipped
  //matching deleteDefinition(VlangConstDefinition, ...)
  //methods are not found in VlangPsiImplUtil

  @NotNull
  String getName();

  @Nullable
  VlangSymbolVisibility getSymbolVisibility();

}
