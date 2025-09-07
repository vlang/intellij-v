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
import com.intellij.psi.ResolveState;
import io.vlang.lang.psi.types.VlangTypeEx;
import io.vlang.lang.stubs.VlangStaticMethodDeclarationStub;
import com.intellij.psi.stubs.IStubElementType;

public class VlangStaticMethodDeclarationImpl extends VlangStaticMethodDeclarationWithScopeHolder implements VlangStaticMethodDeclaration {

  public VlangStaticMethodDeclarationImpl(@NotNull VlangStaticMethodDeclarationStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangStaticMethodDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitStaticMethodDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangAttributes getAttributes() {
    return VlangPsiTreeUtil.getStubChildOfType(this, VlangAttributes.class);
  }

  @Override
  @Nullable
  public VlangBlock getBlock() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangBlock.class);
  }

  @Override
  @Nullable
  public VlangGenericParameters getGenericParameters() {
    return VlangPsiTreeUtil.getStubChildOfType(this, VlangGenericParameters.class);
  }

  @Override
  @Nullable
  public VlangSignature getSignature() {
    return VlangPsiTreeUtil.getStubChildOfType(this, VlangSignature.class);
  }

  @Override
  @Nullable
  public VlangSymbolVisibility getSymbolVisibility() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangSymbolVisibility.class);
  }

  @Override
  @NotNull
  public VlangTypeReferenceExpression getTypeReferenceExpression() {
    return notNullChild(VlangPsiTreeUtil.getStubChildOfType(this, VlangTypeReferenceExpression.class));
  }

  @Override
  @NotNull
  public PsiElement getDot() {
    return notNullChild(findChildByType(DOT));
  }

  @Override
  @NotNull
  public PsiElement getFn() {
    return notNullChild(findChildByType(FN));
  }

  @Override
  @NotNull
  public PsiElement getIdentifier() {
    return notNullChild(findChildByType(IDENTIFIER));
  }

  @Override
  public @Nullable VlangTypeEx getTypeInner(@Nullable ResolveState context) {
    return VlangPsiImplUtil.getTypeInner(this, context);
  }

  @Override
  public @NotNull String getQualifiedName() {
    return VlangPsiImplUtil.getQualifiedName(this);
  }

  @Override
  public @NotNull String getName() {
    return VlangPsiImplUtil.getName(this);
  }

}
