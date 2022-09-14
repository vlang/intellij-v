// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import org.vlang.lang.psi.impl.VlangReference;

public interface VlangTypeReferenceExpression extends VlangReferenceExpressionBase {

  @NotNull
  PsiElement getIdentifier();

  @NotNull
  VlangReference getReference();

  @Nullable
  VlangTypeReferenceExpression getQualifier();

  @Nullable
  PsiElement resolve();

  //WARNING: resolveType(...) is skipped
  //matching resolveType(VlangTypeReferenceExpression, ...)
  //methods are not found in VlangPsiImplUtil

}
