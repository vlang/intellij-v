// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import org.vlang.lang.psi.impl.VlangReference;

public interface VlangReferenceExpression extends VlangExpression, VlangReferenceExpressionBase {

  @NotNull
  PsiElement getIdentifier();

  @NotNull
  VlangReference getReference();

  //WARNING: getQualifier(...) is skipped
  //matching getQualifier(VlangReferenceExpression, ...)
  //methods are not found in VlangPsiImplUtil

  //WARNING: resolve(...) is skipped
  //matching resolve(VlangReferenceExpression, ...)
  //methods are not found in VlangPsiImplUtil

  //WARNING: getReadWriteAccess(...) is skipped
  //matching getReadWriteAccess(VlangReferenceExpression, ...)
  //methods are not found in VlangPsiImplUtil

}
