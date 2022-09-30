// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.StubBasedPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.impl.imports.VlangImportReference;
import org.vlang.lang.stubs.VlangImportNameStub;

public interface VlangImportName extends PsiNameIdentifierOwner, StubBasedPsiElement<VlangImportNameStub> {

  @NotNull
  PsiElement getIdentifier();

  @NotNull
  String getQualifier();

  @NotNull
  PsiElement getNameIdentifier();

  @NotNull
  PsiElement setName(@NotNull String newName);

  @Nullable
  String getName();

  int getTextOffset();

  @NotNull
  VlangImportReference<VlangImportName> getReference();

  @Nullable
  PsiElement resolve();

}
