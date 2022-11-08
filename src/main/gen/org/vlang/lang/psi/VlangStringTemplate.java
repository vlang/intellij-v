// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangStringTemplate extends VlangCompositeElement {

  @NotNull
  List<VlangLongStringTemplateEntry> getLongStringTemplateEntryList();

  @NotNull
  List<VlangShortStringTemplateEntry> getShortStringTemplateEntryList();

  @NotNull
  List<VlangStringTemplateEntry> getStringTemplateEntryList();

  @Nullable
  PsiElement getClosingQuote();

  @NotNull
  PsiElement getOpenQuote();

}
