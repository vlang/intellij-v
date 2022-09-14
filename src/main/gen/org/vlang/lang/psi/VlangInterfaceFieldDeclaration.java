// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

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
