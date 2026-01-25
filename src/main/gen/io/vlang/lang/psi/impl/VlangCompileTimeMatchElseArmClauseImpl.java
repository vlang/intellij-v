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

public class VlangCompileTimeMatchElseArmClauseImpl extends VlangCompositeElementImpl implements VlangCompileTimeMatchElseArmClause {

  public VlangCompileTimeMatchElseArmClauseImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitCompileTimeMatchElseArmClause(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<VlangConstDeclaration> getConstDeclarationList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangConstDeclaration.class);
  }

  @Override
  @NotNull
  public List<VlangEnumDeclaration> getEnumDeclarationList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangEnumDeclaration.class);
  }

  @Override
  @NotNull
  public List<VlangFunctionDeclaration> getFunctionDeclarationList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangFunctionDeclaration.class);
  }

  @Override
  @NotNull
  public List<VlangGlobalVariableDeclaration> getGlobalVariableDeclarationList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangGlobalVariableDeclaration.class);
  }

  @Override
  @NotNull
  public List<VlangInterfaceDeclaration> getInterfaceDeclarationList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangInterfaceDeclaration.class);
  }

  @Override
  @NotNull
  public List<VlangMethodDeclaration> getMethodDeclarationList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangMethodDeclaration.class);
  }

  @Override
  @NotNull
  public List<VlangStatement> getStatementList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangStatement.class);
  }

  @Override
  @NotNull
  public List<VlangStaticMethodDeclaration> getStaticMethodDeclarationList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangStaticMethodDeclaration.class);
  }

  @Override
  @NotNull
  public List<VlangStructDeclaration> getStructDeclarationList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangStructDeclaration.class);
  }

  @Override
  @NotNull
  public List<VlangTypeAliasDeclaration> getTypeAliasDeclarationList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangTypeAliasDeclaration.class);
  }

  @Override
  @NotNull
  public PsiElement getElseCompileTime() {
    return notNullChild(findChildByType(ELSE_COMPILE_TIME));
  }

  @Override
  @Nullable
  public PsiElement getLbrace() {
    return findChildByType(LBRACE);
  }

  @Override
  @Nullable
  public PsiElement getRbrace() {
    return findChildByType(RBRACE);
  }

}
