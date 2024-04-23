// This is a generated file. Not intended for manual editing.
package io.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import io.vlang.lang.stubs.VlangImportAliasStub;

public interface VlangImportAlias extends VlangNamedElement, StubBasedPsiElement<VlangImportAliasStub> {

  @Nullable
  VlangImportAliasName getImportAliasName();

  @NotNull
  PsiElement getAs();

  @NotNull
  String getName();

  @Nullable
  PsiElement getIdentifier();

  @NotNull
  String getQualifiedName();

}
