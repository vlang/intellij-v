// This is a generated file. Not intended for manual editing.
package io.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangSendStatement extends VlangStatement {

  @Nullable
  VlangConstDeclaration getConstDeclaration();

  @Nullable
  VlangEnumDeclaration getEnumDeclaration();

  @Nullable
  VlangExpression getExpression();

  @Nullable
  VlangFunctionDeclaration getFunctionDeclaration();

  @Nullable
  VlangGlobalVariableDeclaration getGlobalVariableDeclaration();

  @Nullable
  VlangImportList getImportList();

  @Nullable
  VlangInterfaceDeclaration getInterfaceDeclaration();

  @Nullable
  VlangLabelDefinition getLabelDefinition();

  @Nullable
  VlangMethodDeclaration getMethodDeclaration();

  @Nullable
  VlangModuleClause getModuleClause();

  @Nullable
  VlangSelectArm getSelectArm();

  @Nullable
  VlangSelectElseArmClause getSelectElseArmClause();

  @Nullable
  VlangShebangClause getShebangClause();

  @Nullable
  VlangStatement getStatement();

  @Nullable
  VlangStaticMethodDeclaration getStaticMethodDeclaration();

  @Nullable
  VlangStructDeclaration getStructDeclaration();

  @Nullable
  VlangTypeAliasDeclaration getTypeAliasDeclaration();

  @NotNull
  PsiElement getSendChannel();

}
