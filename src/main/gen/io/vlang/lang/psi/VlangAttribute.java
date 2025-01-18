// This is a generated file. Not intended for manual editing.
package io.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import io.vlang.lang.stubs.VlangAttributeStub;

public interface VlangAttribute extends VlangCompositeElement, StubBasedPsiElement<VlangAttributeStub> {

  @NotNull
  List<VlangAttributeExpression> getAttributeExpressionList();

  @Nullable
  PsiElement getAtLbrack();

  @Nullable
  PsiElement getLbrack();

  @Nullable
  PsiElement getRbrack();

}
