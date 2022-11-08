// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangFieldDeclaration extends VlangCompositeElement {

  @Nullable
  VlangAttribute getAttribute();

  @Nullable
  VlangDefaultFieldValue getDefaultFieldValue();

  @Nullable
  VlangEmbeddedDefinition getEmbeddedDefinition();

  @Nullable
  VlangFieldDefinition getFieldDefinition();

  @Nullable
  VlangType getType();

  @Nullable
  PsiElement getSemicolon();

  @Nullable
  PsiElement getSemicolonSynthetic();

}
