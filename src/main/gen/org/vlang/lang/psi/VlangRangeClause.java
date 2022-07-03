// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface VlangRangeClause extends VlangShortVarDeclaration {

  @Nullable
  VlangExpression getExpression();

  @NotNull
  List<VlangVarDefinition> getVarDefinitionList();

  @NotNull
  PsiElement getIn();

  //WARNING: getRangeExpression(...) is skipped
  //matching getRangeExpression(VlangRangeClause, ...)
  //methods are not found in VlangPsiImplUtil

  //WARNING: getLeftExpressionsList(...) is skipped
  //matching getLeftExpressionsList(VlangRangeClause, ...)
  //methods are not found in VlangPsiImplUtil

  //WARNING: getRightExpressionsList(...) is skipped
  //matching getRightExpressionsList(VlangRangeClause, ...)
  //methods are not found in VlangPsiImplUtil

}
