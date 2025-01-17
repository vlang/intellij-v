// This is a generated file. Not intended for manual editing.
package io.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import io.vlang.lang.psi.impl.VlangModule;

public interface VlangImportSpec extends VlangCompositeElement {

  @Nullable
  VlangImportAlias getImportAlias();

  @NotNull
  VlangImportPath getImportPath();

  @Nullable
  VlangSelectiveImportList getSelectiveImportList();

  @NotNull PsiElement getIdentifier();

  @NotNull String getLastPart();

  @NotNull PsiElement getLastPartPsi();

  @NotNull String getName();

  @NotNull String getImportedName();

  @Nullable String getAliasName();

  @NotNull String getPathName();

  @NotNull List<@NotNull VlangModule> resolve();

}
