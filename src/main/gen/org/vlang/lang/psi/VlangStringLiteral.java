// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VlangStringLiteral extends VlangExpression {

  @Nullable
  VlangStringTemplate getStringTemplate();

  @Nullable
  PsiElement getRawString();

  //WARNING: isValidHost(...) is skipped
  //matching isValidHost(VlangStringLiteral, ...)
  //methods are not found in VlangPsiImplUtil

  //WARNING: updateText(...) is skipped
  //matching updateText(VlangStringLiteral, ...)
  //methods are not found in VlangPsiImplUtil

  //WARNING: createLiteralTextEscaper(...) is skipped
  //matching createLiteralTextEscaper(VlangStringLiteral, ...)
  //methods are not found in VlangPsiImplUtil

  //WARNING: getDecodedText(...) is skipped
  //matching getDecodedText(VlangStringLiteral, ...)
  //methods are not found in VlangPsiImplUtil

  @NotNull
  String getContents();

}
