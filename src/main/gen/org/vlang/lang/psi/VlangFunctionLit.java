// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangFunctionLit extends VlangExpression, VlangSignatureOwner, VlangGenericParametersOwner {

  @Nullable
  VlangBlock getBlock();

  @Nullable
  VlangCaptureList getCaptureList();

  @Nullable
  VlangGenericParameters getGenericParameters();

  @Nullable
  VlangSignature getSignature();

  @NotNull
  PsiElement getFn();

  @NotNull
  PsiElement addCapture(@NotNull String name);

  //WARNING: processDeclarations(...) is skipped
  //matching processDeclarations(VlangFunctionLit, ...)
  //methods are not found in VlangPsiImplUtil

}
