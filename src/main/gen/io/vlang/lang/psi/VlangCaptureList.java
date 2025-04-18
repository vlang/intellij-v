// This is a generated file. Not intended for manual editing.
package io.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangCaptureList extends VlangCompositeElement {

  @NotNull
  List<VlangCapture> getCaptureList();

  @NotNull
  PsiElement getLbrack();

  @Nullable
  PsiElement getRbrack();

  @NotNull PsiElement addCapture(@NotNull String name);

}
