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

public class VlangSqlSelectStatementImpl extends VlangSqlBlockStatementImpl implements VlangSqlSelectStatement {

  public VlangSqlSelectStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitSqlSelectStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangSqlFromClause getSqlFromClause() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangSqlFromClause.class);
  }

  @Override
  @Nullable
  public VlangSqlLimitClause getSqlLimitClause() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangSqlLimitClause.class);
  }

  @Override
  @Nullable
  public VlangSqlOffsetClause getSqlOffsetClause() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangSqlOffsetClause.class);
  }

  @Override
  @Nullable
  public VlangSqlOrderByClause getSqlOrderByClause() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangSqlOrderByClause.class);
  }

  @Override
  @Nullable
  public VlangSqlSelectCountClause getSqlSelectCountClause() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangSqlSelectCountClause.class);
  }

  @Override
  @Nullable
  public VlangSqlWhereClause getSqlWhereClause() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangSqlWhereClause.class);
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

}
