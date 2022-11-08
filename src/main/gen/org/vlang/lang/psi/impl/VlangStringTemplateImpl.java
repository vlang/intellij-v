// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import static org.vlang.lang.VlangTypes.*;
import org.vlang.lang.psi.*;

public class VlangStringTemplateImpl extends VlangCompositeElementImpl implements VlangStringTemplate {

  public VlangStringTemplateImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitStringTemplate(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<VlangLongStringTemplateEntry> getLongStringTemplateEntryList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangLongStringTemplateEntry.class);
  }

  @Override
  @NotNull
  public List<VlangShortStringTemplateEntry> getShortStringTemplateEntryList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangShortStringTemplateEntry.class);
  }

  @Override
  @NotNull
  public List<VlangStringTemplateEntry> getStringTemplateEntryList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangStringTemplateEntry.class);
  }

  @Override
  @Nullable
  public PsiElement getClosingQuote() {
    return findChildByType(CLOSING_QUOTE);
  }

  @Override
  @NotNull
  public PsiElement getOpenQuote() {
    return notNullChild(findChildByType(OPEN_QUOTE));
  }

}
