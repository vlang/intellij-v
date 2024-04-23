// This is a generated file. Not intended for manual editing.
package io.vlang.lang.vmod.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static io.vlang.lang.vmod.VmodTypes.*;
import io.vlang.vmod.psi.impl.VmodCompositeElementImpl;
import io.vlang.lang.vmod.psi.*;

public class VmodFieldImpl extends VmodCompositeElementImpl implements VmodField {

  public VmodFieldImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VmodVisitor visitor) {
    visitor.visitField(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VmodVisitor) accept((VmodVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public VmodExpression getExpression() {
    return findNotNullChildByClass(VmodExpression.class);
  }

  @Override
  @NotNull
  public VmodFieldName getFieldName() {
    return findNotNullChildByClass(VmodFieldName.class);
  }

  @Override
  @NotNull
  public PsiElement getColon() {
    return findNotNullChildByType(COLON);
  }

}
