// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.*;

import static org.vlang.lang.VlangTypes.SEND_CHANNEL;

public class VlangSendStatementImpl extends VlangStatementImpl implements VlangSendStatement {

  public VlangSendStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitSendStatement(this);
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
  public VlangImportList getImportList() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangImportList.class);
  }

  @Override
  @Nullable
  public VlangLabelDefinition getLabelDefinition() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangLabelDefinition.class);
  }

  @Override
  @Nullable
  public VlangModuleClause getModuleClause() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangModuleClause.class);
  }

  @Override
  @Nullable
  public VlangStatement getStatement() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangStatement.class);
  }

  @Override
  @NotNull
  public PsiElement getSendChannel() {
    return notNullChild(findChildByType(SEND_CHANNEL));
  }

}
