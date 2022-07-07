// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangFieldDeclaration extends VlangCompositeElement {

  @Nullable
  VlangAnonymousFieldDefinition getAnonymousFieldDefinition();

  @Nullable
  VlangAttribute getAttribute();

  @Nullable
  VlangDefaultFieldValue getDefaultFieldValue();

  @NotNull
  List<VlangFieldName> getFieldNameList();

  @Nullable
  VlangTag getTag();

  @Nullable
  VlangTypeDecl getTypeDecl();

  @Nullable
  PsiElement getShared();

}
