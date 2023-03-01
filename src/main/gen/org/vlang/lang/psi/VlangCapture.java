// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangCapture extends VlangCompositeElement {

  @NotNull
  VlangReferenceExpression getReferenceExpression();

  @Nullable
  VlangVarModifiers getVarModifiers();

  boolean isMutable();

  void makeMutable();

}
