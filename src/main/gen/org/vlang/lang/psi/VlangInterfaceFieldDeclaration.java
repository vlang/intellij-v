// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface VlangInterfaceFieldDeclaration extends VlangCompositeElement {

  @Nullable
  VlangAttribute getAttribute();

  @NotNull
  List<VlangFieldName> getFieldNameList();

  @Nullable
  VlangTag getTag();

  @NotNull
  VlangType getType();

}
