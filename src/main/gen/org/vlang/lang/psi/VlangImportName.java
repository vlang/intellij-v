// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.StubBasedPsiElement;
import org.vlang.lang.stubs.VlangImportNameStub;
import org.vlang.lang.psi.impl.imports.VlangImportReference;

public interface VlangImportName extends PsiNameIdentifierOwner, VlangCompositeElement, StubBasedPsiElement<VlangImportNameStub> {

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
