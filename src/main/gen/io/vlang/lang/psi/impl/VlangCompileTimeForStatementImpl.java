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

public class VlangCompileTimeForStatementImpl extends VlangStatementImpl implements VlangCompileTimeForStatement {

  public VlangCompileTimeForStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitCompileTimeForStatement(this);
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
  public VlangForClause getForClause() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangForClause.class);
  }

  @Override
  @Nullable
  public VlangRangeClause getRangeClause() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangRangeClause.class);
  }

  @Override
  @NotNull
  public PsiElement getForCompileTime() {
    return notNullChild(findChildByType(FOR_COMPILE_TIME));
  }

}
