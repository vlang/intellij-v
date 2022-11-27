// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import org.vlang.lang.psi.impl.imports.VlangModuleReference;

public interface VlangImportAliasName extends VlangCompositeElement {

  @NotNull
  PsiElement getIdentifier();

  @NotNull
  VlangModuleReference<VlangImportAliasName> getReference();

}
