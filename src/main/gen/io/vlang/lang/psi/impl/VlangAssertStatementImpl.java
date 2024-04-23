// This is a generated file. Not intended for manual editing.
package io.vlang.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import io.vlang.lang.psi.VlangPsiTreeUtil;
import static io.vlang.lang.VlangTypes.*;
import io.vlang.lang.psi.*;

public class VlangAssertStatementImpl extends VlangStatementImpl implements VlangAssertStatement {

  public VlangAssertStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitAssertStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<VlangExpression> getExpressionList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangExpression.class);
  }

  @Override
  @Nullable
  public PsiElement getComma() {
    return findChildByType(COMMA);
  }

  @Override
  @NotNull
  public PsiElement getAssert() {
    return notNullChild(findChildByType(ASSERT));
  }

}
