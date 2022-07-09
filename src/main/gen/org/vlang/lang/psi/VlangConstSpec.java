// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangConstSpec extends VlangCompositeElement {

  @NotNull
  VlangConstDefinition getConstDefinition();

  @Nullable
  VlangExpression getExpression();

  @NotNull
  PsiElement getAssign();

  //WARNING: deleteDefinition(...) is skipped
  //matching deleteDefinition(VlangConstSpec, ...)
  //methods are not found in VlangPsiImplUtil

}
