// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import org.vlang.lang.psi.VlangSqlUpdateItem;
import org.vlang.lang.psi.VlangSqlUpdateList;
import org.vlang.lang.psi.VlangVisitor;

import java.util.List;

public class VlangSqlUpdateListImpl extends VlangCompositeElementImpl implements VlangSqlUpdateList {

  public VlangSqlUpdateListImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitSqlUpdateList(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<VlangSqlUpdateItem> getSqlUpdateItemList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangSqlUpdateItem.class);
  }

}
