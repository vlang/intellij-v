// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import org.vlang.lang.psi.VlangSqlReferenceList;
import org.vlang.lang.psi.VlangSqlReferenceListItem;
import org.vlang.lang.psi.VlangVisitor;

import java.util.List;

public class VlangSqlReferenceListImpl extends VlangCompositeElementImpl implements VlangSqlReferenceList {

  public VlangSqlReferenceListImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitSqlReferenceList(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<VlangSqlReferenceListItem> getSqlReferenceListItemList() {
    return VlangPsiTreeUtil.getChildrenOfTypeAsList(this, VlangSqlReferenceListItem.class);
  }

}
