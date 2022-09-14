// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangParamDefinition extends VlangNamedElement {

  @Nullable
  VlangVarModifiers getVarModifiers();

  @NotNull
  PsiElement getIdentifier();

  //WARNING: isVariadic(...) is skipped
  //matching isVariadic(VlangParamDefinition, ...)
  //methods are not found in VlangPsiImplUtil

  @NotNull
  String getName();

}
