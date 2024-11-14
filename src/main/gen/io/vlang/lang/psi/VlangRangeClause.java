// This is a generated file. Not intended for manual editing.
package io.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangRangeClause extends VlangVarDeclaration {

  @Nullable
  VlangExpression getExpression();

  @NotNull
  PsiElement getIn();

  //WARNING: getRangeExpression(...) is skipped
  //matching getRangeExpression(VlangRangeClause, ...)
  //methods are not found in VlangPsiImplUtil

  @NotNull List<@NotNull VlangVarDefinition> getVariablesList();

}
