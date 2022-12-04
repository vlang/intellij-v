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

public class VlangLongStringTemplateEntryImpl extends VlangCompositeElementImpl implements VlangLongStringTemplateEntry {

  public VlangLongStringTemplateEntryImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitLongStringTemplateEntry(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangExpression getExpression() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangExpression.class);
  }

  @Override
  @Nullable
  public VlangFormatSpecifier getFormatSpecifier() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangFormatSpecifier.class);
  }

  @Override
  @NotNull
  public PsiElement getLongTemplateEntryStart() {
    return notNullChild(findChildByType(LONG_TEMPLATE_ENTRY_START));
  }

  @Override
  @Nullable
  public PsiElement getSemicolon() {
    return findChildByType(SEMICOLON);
  }

  @Override
  @Nullable
  public PsiElement getSemicolonSynthetic() {
    return findChildByType(SEMICOLON_SYNTHETIC);
  }

  @Override
  @Nullable
  public PsiElement getTemplateEntryEnd() {
    return findChildByType(TEMPLATE_ENTRY_END);
  }

}
