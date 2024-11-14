// This is a generated file. Not intended for manual editing.
package io.vlang.lang.psi;

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

  @NotNull List<@NotNull VlangMatchArm> getArms();

  @NotNull List<@NotNull VlangExpression> getExpressionArms();

  @NotNull List<@NotNull VlangType> getTypeArms();

  @Nullable VlangMatchElseArmClause getElseArm();

  boolean withElse();

}
