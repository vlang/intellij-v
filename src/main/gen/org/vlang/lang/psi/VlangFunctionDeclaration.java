// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.impl.VlangReference;
import org.vlang.lang.stubs.VlangFunctionDeclarationStub;

public interface VlangFunctionDeclaration extends VlangFunctionOrMethodDeclaration, VlangReferenceExpressionBase, StubBasedPsiElement<VlangFunctionDeclarationStub> {

  @Nullable
  VlangBlock getBlock();

  @Nullable
  VlangSignature getSignature();

  @NotNull
  VlangSymbolVisibility getSymbolVisibility();

  @NotNull
  PsiElement getFunc();

  @Nullable
  PsiElement getIdentifier();

  @NotNull
  VlangReference getReference();

}
