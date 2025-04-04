// This is a generated file. Not intended for manual editing.
package io.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import io.vlang.lang.psi.impl.VlangReference;

public interface VlangEnumFetch extends VlangExpression, VlangReferenceExpressionBase {

  @NotNull
  PsiElement getDot();

  @NotNull
  PsiElement getIdentifier();

  @NotNull VlangReference getReference();

  @Nullable VlangCompositeElement getQualifier();

  @Nullable PsiElement resolve();

}
