// This is a generated file. Not intended for manual editing.
package io.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangImportPath extends VlangCompositeElement {

  @NotNull
  List<VlangImportName> getImportNameList();

  @NotNull String getQualifiedName();

  @NotNull String getLastPart();

  @NotNull PsiElement getLastPartPsi();

}
