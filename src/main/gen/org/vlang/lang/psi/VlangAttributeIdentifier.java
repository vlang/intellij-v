// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import org.vlang.lang.psi.impl.VlangAttributeReference;

public interface VlangAttributeIdentifier extends VlangCompositeElement {

  @Nullable
  VlangAttributeIdentifierPrefix getAttributeIdentifierPrefix();

  @Nullable
  PsiElement getIdentifier();

  @Nullable
  PsiElement getUnsafe();

  @NotNull
  VlangAttributeReference getReference();

}
