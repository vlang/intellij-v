// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VlangStringLiteral extends VlangExpression, PsiLanguageInjectionHost {

  @Nullable
  VlangStringTemplate getStringTemplate();

  @Nullable
  PsiElement getRawString();

  boolean isValidHost();

  @NotNull
  VlangStringLiteral updateText(@NotNull String text);

  @NotNull
  StringLiteralEscaper<VlangStringLiteral> createLiteralTextEscaper();

  //WARNING: getDecodedText(...) is skipped
  //matching getDecodedText(VlangStringLiteral, ...)
  //methods are not found in VlangPsiImplUtil

  @NotNull
  String getContents();

}
