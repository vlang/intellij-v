// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangLiteralValueExpression extends VlangExpression {

  @NotNull
  List<VlangElement> getElementList();

  @Nullable
  VlangGenericArguments getGenericArguments();

  @NotNull
  VlangType getType();

  @NotNull
  PsiElement getLbrace();

  @NotNull
  PsiElement getRbrace();

}
