// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VlangImportSpec extends VlangNamedElement {

  @Nullable
  VlangImportAlias getImportAlias();

  @NotNull
  VlangImportPath getImportPath();

  @Nullable
  VlangSelectiveImportList getSelectiveImportList();

  @NotNull
  PsiElement getIdentifier();

  //WARNING: getAlias(...) is skipped
  //matching getAlias(VlangImportSpec, ...)
  //methods are not found in VlangPsiImplUtil

  @NotNull
  String getLastPart();

  //WARNING: getLocalPackageName(...) is skipped
  //matching getLocalPackageName(VlangImportSpec, ...)
  //methods are not found in VlangPsiImplUtil

  //WARNING: shouldGoDeeper(...) is skipped
  //matching shouldGoDeeper(VlangImportSpec, ...)
  //methods are not found in VlangPsiImplUtil

  //WARNING: isForSideEffects(...) is skipped
  //matching isForSideEffects(VlangImportSpec, ...)
  //methods are not found in VlangPsiImplUtil

  //WARNING: isDot(...) is skipped
  //matching isDot(VlangImportSpec, ...)
  //methods are not found in VlangPsiImplUtil

  //WARNING: getPath(...) is skipped
  //matching getPath(VlangImportSpec, ...)
  //methods are not found in VlangPsiImplUtil

  @NotNull
  String getName();

  //WARNING: isCImport(...) is skipped
  //matching isCImport(VlangImportSpec, ...)
  //methods are not found in VlangPsiImplUtil

}
