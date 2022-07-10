// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangTypeDecl extends VlangReferenceExpressionBase {

  @Nullable
  VlangGenericDeclaration getGenericDeclaration();

  @Nullable
  VlangTypeDecl getTypeDecl();

  @NotNull
  List<VlangTypeReferenceExpression> getTypeReferenceExpressionList();

  @Nullable
  PsiElement getIdentifier();

}
