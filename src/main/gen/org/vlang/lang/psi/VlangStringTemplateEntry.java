// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangStringTemplateEntry extends VlangCompositeElement {

  @Nullable
  VlangExpression getExpression();

  @Nullable
  VlangFormatSpecifier getFormatSpecifier();

  @Nullable
  PsiElement getTemplateEntryEnd();

  @NotNull
  PsiElement getTemplateEntryStart();

}
