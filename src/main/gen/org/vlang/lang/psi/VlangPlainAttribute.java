// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.vlang.lang.stubs.VlangPlainAttributeStub;

public interface VlangPlainAttribute extends VlangCompositeElement, StubBasedPsiElement<VlangPlainAttributeStub> {

  @NotNull
  VlangAttributeKey getAttributeKey();

  @Nullable
  VlangAttributeValue getAttributeValue();

  @Nullable
  PsiElement getColon();

}
