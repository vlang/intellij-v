// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
  VlangType getType();

}
