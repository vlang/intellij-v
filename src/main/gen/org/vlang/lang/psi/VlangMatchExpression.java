// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangMatchExpression extends VlangExpression {

  @Nullable
  VlangExpression getExpression();

  @Nullable
  VlangMatchArms getMatchArms();

  @Nullable
  PsiElement getLbrace();

  @Nullable
  PsiElement getRbrace();

  @NotNull
  PsiElement getMatch();

  @NotNull
  List<VlangMatchArm> getArms();

  @NotNull
  List<VlangExpression> getExpressionArms();

  @NotNull
  List<VlangType> getTypeArms();

  @Nullable
  VlangMatchElseArmClause getElseArm();

  boolean withElse();

}
