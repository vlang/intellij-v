// This is a generated file. Not intended for manual editing.
package io.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangCompileTimeMatchArm extends VlangCompositeElement {

  @NotNull
  List<VlangConstDeclaration> getConstDeclarationList();

  @NotNull
  List<VlangEnumDeclaration> getEnumDeclarationList();

  @NotNull
  List<VlangFunctionDeclaration> getFunctionDeclarationList();

  @NotNull
  List<VlangGlobalVariableDeclaration> getGlobalVariableDeclarationList();

  @NotNull
  List<VlangInterfaceDeclaration> getInterfaceDeclarationList();

  @NotNull
  List<VlangMethodDeclaration> getMethodDeclarationList();

  @NotNull
  List<VlangStatement> getStatementList();

  @NotNull
  List<VlangStaticMethodDeclaration> getStaticMethodDeclarationList();

  @NotNull
  List<VlangStructDeclaration> getStructDeclarationList();

  @NotNull
  List<VlangTypeAliasDeclaration> getTypeAliasDeclarationList();

  @NotNull
  PsiElement getLbrace();

  @Nullable
  PsiElement getRbrace();

  //WARNING: getParameterList(...) is skipped
  //matching getParameterList(VlangCompileTimeMatchArm, ...)
  //methods are not found in VlangPsiImplUtil

}
