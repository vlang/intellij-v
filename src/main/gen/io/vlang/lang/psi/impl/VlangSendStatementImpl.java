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
  public VlangConstDeclaration getConstDeclaration() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangConstDeclaration.class);
  }

  @Override
  @Nullable
  public VlangEnumDeclaration getEnumDeclaration() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangEnumDeclaration.class);
  }

  @Override
  @Nullable
  public VlangExpression getExpression() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangExpression.class);
  }

  @Override
  @Nullable
  public VlangFunctionDeclaration getFunctionDeclaration() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangFunctionDeclaration.class);
  }

  @Override
  @Nullable
  public VlangGlobalVariableDeclaration getGlobalVariableDeclaration() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangGlobalVariableDeclaration.class);
  }

  @Override
  @Nullable
  public VlangImportList getImportList() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangImportList.class);
  }

  @Override
  @Nullable
  public VlangInterfaceDeclaration getInterfaceDeclaration() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangInterfaceDeclaration.class);
  }

  @Override
  @Nullable
  public VlangLabelDefinition getLabelDefinition() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangLabelDefinition.class);
  }

  @Override
  @Nullable
  public VlangMethodDeclaration getMethodDeclaration() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangMethodDeclaration.class);
  }

  @Override
  @Nullable
  public VlangModuleClause getModuleClause() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangModuleClause.class);
  }

  @Override
  @Nullable
  public VlangSelectArm getSelectArm() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangSelectArm.class);
  }

  @Override
  @Nullable
  public VlangSelectElseArmClause getSelectElseArmClause() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangSelectElseArmClause.class);
  }

  @Override
  @Nullable
  public VlangShebangClause getShebangClause() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangShebangClause.class);
  }

  @Override
  @Nullable
  public VlangStatement getStatement() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangStatement.class);
  }

  @Override
  @Nullable
  public VlangStaticMethodDeclaration getStaticMethodDeclaration() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangStaticMethodDeclaration.class);
  }

  @Override
  @Nullable
  public VlangStructDeclaration getStructDeclaration() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangStructDeclaration.class);
  }

  @Override
  @Nullable
  public VlangTypeAliasDeclaration getTypeAliasDeclaration() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangTypeAliasDeclaration.class);
  }

  @Override
  @NotNull
  public PsiElement getSendChannel() {
    return notNullChild(findChildByType(SEND_CHANNEL));
  }

}
