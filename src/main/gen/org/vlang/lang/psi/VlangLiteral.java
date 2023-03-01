// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangLiteral extends VlangExpression {

  @Nullable
  PsiElement getBin();

  @Nullable
  PsiElement getChar();

  @Nullable
  PsiElement getFalse();

  @Nullable
  PsiElement getFloat();

  @Nullable
  PsiElement getHex();

  @Nullable
  PsiElement getInt();

  @Nullable
  PsiElement getNil();

  @Nullable
  PsiElement getNone();

  @Nullable
  PsiElement getOct();

  @Nullable
  PsiElement getTrue();

  boolean isNumeric();

  boolean isBoolean();

}
