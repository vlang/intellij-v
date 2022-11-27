// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.vlang.lang.stubs.VlangImportNameStub;
import org.vlang.lang.psi.impl.VlangModule;
import org.vlang.lang.psi.impl.imports.VlangModuleReference;

public interface VlangImportName extends VlangNamedElement, StubBasedPsiElement<VlangImportNameStub> {

  @NotNull
  PsiElement getIdentifier();

  @NotNull
  String getQualifier();

  @NotNull
  PsiElement getNameIdentifier();

  @NotNull
  PsiElement setName(@NotNull String newName);

  @NotNull
  String getName();

  int getTextOffset();

  @NotNull
  VlangModuleReference<VlangImportName> getReference();

  @NotNull
  List<VlangModule> resolve();

  @NotNull
  String getQualifiedName();

}
