// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VlangFunctionLit extends VlangExpression, VlangSignatureOwner {

  @Nullable
  VlangBlock getBlock();

  @Nullable
  VlangCaptureList getCaptureList();

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
