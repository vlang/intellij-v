// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangLongStringTemplateEntry extends VlangCompositeElement {

  @Nullable
  VlangExpression getExpression();

  @Nullable
  VlangFormatSpecifier getFormatSpecifier();

  @NotNull
  PsiElement getLongTemplateEntryStart();

  @Nullable
  PsiElement getSemicolon();

  @Nullable
  PsiElement getSemicolonSynthetic();

  @Nullable
  PsiElement getTemplateEntryEnd();

}
