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

public class VlangSqlUpdateStatementImpl extends VlangSqlBlockStatementImpl implements VlangSqlUpdateStatement {

  public VlangSqlUpdateStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitSqlUpdateStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangSqlTableName getSqlTableName() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangSqlTableName.class);
  }

  @Override
  @Nullable
  public VlangSqlUpdateList getSqlUpdateList() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangSqlUpdateList.class);
  }

  @Override
  @Nullable
  public VlangSqlWhereClause getSqlWhereClause() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangSqlWhereClause.class);
  }

}
