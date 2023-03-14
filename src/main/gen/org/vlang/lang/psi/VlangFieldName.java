// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangFieldName extends VlangReferenceExpressionBase {

  @NotNull
  VlangReferenceExpression getReferenceExpression();

  @NotNull
  PsiElement getIdentifier();

  @Nullable
  VlangCompositeElement getQualifier();

  @Nullable
  PsiElement resolve();

}
