// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.vlang.lang.psi.VlangLanguageInjectionStatement;
import org.vlang.lang.psi.VlangVisitor;

import static org.vlang.lang.VlangTypes.LANGUAGE_INJECTION;

public class VlangLanguageInjectionStatementImpl extends VlangStatementImpl implements VlangLanguageInjectionStatement {

  public VlangLanguageInjectionStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitLanguageInjectionStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiElement getLanguageInjection() {
    return notNullChild(findChildByType(LANGUAGE_INJECTION));
  }

}
