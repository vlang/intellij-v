// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VlangInterfaceType extends VlangTypeDecl {

  @Nullable
  VlangAnonymousInterfaceDefinition getAnonymousInterfaceDefinition();

  @Nullable
  VlangInterfaceFieldDeclaration getInterfaceFieldDeclaration();

  @Nullable
  VlangInterfaceMethodDeclaration getInterfaceMethodDeclaration();

  @Nullable
  VlangMemberModifiers getMemberModifiers();

  @Nullable
  PsiElement getLbrace();

  @Nullable
  PsiElement getRbrace();

  @Nullable
  PsiElement getIdentifier();

  @NotNull
  PsiElement getInterface();

}
