// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.vlang.lang.stubs.VlangMethodDeclarationStub;

public interface VlangMethodDeclaration extends VlangFunctionOrMethodDeclaration, StubBasedPsiElement<VlangMethodDeclarationStub> {

  @Nullable
  VlangAttributes getAttributes();

  @Nullable
  VlangBlock getBlock();

  @Nullable
  VlangGenericDeclaration getGenericDeclaration();

  @NotNull
  VlangMethodName getMethodName();

  @NotNull
  VlangReceiver getReceiver();

  @Nullable
  VlangSignature getSignature();

  @Nullable
  VlangSymbolVisibility getSymbolVisibility();

  @NotNull
  PsiElement getFn();

  //WARNING: getReceiverType(...) is skipped
  //matching getReceiverType(VlangMethodDeclaration, ...)
  //methods are not found in VlangPsiImplUtil

  @Nullable
  PsiElement getIdentifier();

}
