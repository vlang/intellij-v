// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangTypeStatement extends VlangStatement {

  @NotNull
  VlangSymbolVisibility getSymbolVisibility();

  @Nullable
  VlangTypeUnionList getTypeUnionList();

  @Nullable
  PsiElement getAssign();

  @NotNull
  PsiElement getType_();

  @Nullable
  PsiElement getIdentifier();

}
