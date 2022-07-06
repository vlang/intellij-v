// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

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
