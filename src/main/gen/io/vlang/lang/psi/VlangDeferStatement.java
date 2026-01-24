// This is a generated file. Not intended for manual editing.
package io.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangDeferStatement extends VlangStatement {

  @NotNull
  VlangBlock getBlock();

  @Nullable
  PsiElement getLparen();

  @Nullable
  PsiElement getRparen();

  @NotNull
  PsiElement getDefer();

  @Nullable
  PsiElement getFn();

}
