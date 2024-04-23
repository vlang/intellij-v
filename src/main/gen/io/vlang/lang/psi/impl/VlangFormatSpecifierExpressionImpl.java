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

public class VlangFormatSpecifierExpressionImpl extends VlangCompositeElementImpl implements VlangFormatSpecifierExpression {

  public VlangFormatSpecifierExpressionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitFormatSpecifierExpression(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public VlangFormatSpecifierLeftAlignFlag getFormatSpecifierLeftAlignFlag() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangFormatSpecifierLeftAlignFlag.class);
  }

  @Override
  @Nullable
  public VlangFormatSpecifierLetter getFormatSpecifierLetter() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangFormatSpecifierLetter.class);
  }

  @Override
  @Nullable
  public VlangFormatSpecifierRightAlignFlag getFormatSpecifierRightAlignFlag() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangFormatSpecifierRightAlignFlag.class);
  }

  @Override
  @Nullable
  public VlangFormatSpecifierWidthAndPrecision getFormatSpecifierWidthAndPrecision() {
    return VlangPsiTreeUtil.getChildOfType(this, VlangFormatSpecifierWidthAndPrecision.class);
  }

}
