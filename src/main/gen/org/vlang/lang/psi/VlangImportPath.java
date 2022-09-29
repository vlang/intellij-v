// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.impl.imports.VlangImportReference;
import org.vlang.lang.stubs.VlangImportPathStub;

import java.util.List;

public interface VlangImportPath extends VlangNamedElement, VlangReferenceExpressionBase, StubBasedPsiElement<VlangImportPathStub> {

  @NotNull
  List<VlangImportName> getImportNameList();

  @Nullable
  PsiElement getIdentifier();

  @Nullable
  VlangCompositeElement getQualifier();

  @NotNull
  VlangImportReference<VlangImportPath> getReference();

}
