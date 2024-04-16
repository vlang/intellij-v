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
import com.intellij.psi.stubs.IStubElementType;
import io.vlang.lang.stubs.VlangTypeStub;

public class VlangAnonymousStructTypeImpl extends VlangTypeImpl implements VlangAnonymousStructType {

  public VlangAnonymousStructTypeImpl(@NotNull VlangTypeStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public VlangAnonymousStructTypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitAnonymousStructType(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<VlangFieldsGroup> getFieldsGroupList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangFieldsGroup.class);
  }

  @Override
  @NotNull
  public PsiElement getLbrace() {
    return notNullChild(findChildByType(LBRACE));
  }

  @Override
  @Nullable
  public PsiElement getRbrace() {
    return findChildByType(RBRACE);
  }

  @Override
  @NotNull
  public PsiElement getStruct() {
    return notNullChild(findChildByType(STRUCT));
  }

  @Override
  @NotNull
  public List<VlangFieldDefinition> getFieldList() {
    return VlangPsiImplUtil.getFieldList(this);
  }

  @Override
  @NotNull
  public List<VlangFieldDefinition> getOwnFieldList() {
    return VlangPsiImplUtil.getOwnFieldList(this);
  }

}
