// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangInterfaceMethodDeclaration extends VlangCompositeElement {

  @Nullable
  VlangAttribute getAttribute();

  @Nullable
  VlangDefaultFieldValue getDefaultFieldValue();

  @NotNull
  VlangInterfaceMethodDefinition getInterfaceMethodDefinition();

  @Nullable
  PsiElement getSemicolon();

  @Nullable
  PsiElement getSemicolonSynthetic();

}
