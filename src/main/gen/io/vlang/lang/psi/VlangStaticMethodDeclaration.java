// This is a generated file. Not intended for manual editing.
package io.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import io.vlang.lang.stubs.VlangStaticMethodDeclarationStub;
import com.intellij.psi.ResolveState;
import io.vlang.lang.psi.types.VlangTypeEx;

public interface VlangStaticMethodDeclaration extends VlangSignatureOwner, VlangFunctionOrMethodDeclaration, VlangAttributeOwner, VlangGenericParametersOwner, VlangScopeHolder, StubBasedPsiElement<VlangStaticMethodDeclarationStub> {

  @Nullable
  VlangAttributes getAttributes();

  @Nullable
  VlangBlock getBlock();

  @Nullable
  VlangGenericParameters getGenericParameters();

  @Nullable
  VlangSignature getSignature();

  @Nullable
  VlangSymbolVisibility getSymbolVisibility();

  @NotNull
  VlangTypeReferenceExpression getTypeReferenceExpression();

  @NotNull
  PsiElement getDot();

  @NotNull
  PsiElement getFn();

  @Nullable
  PsiElement getIdentifier();

  //WARNING: getType(...) is skipped
  //matching getType(VlangStaticMethodDeclaration, ...)
  //methods are not found in VlangPsiImplUtil

  @Nullable VlangTypeEx getTypeInner(@Nullable ResolveState context);

  @NotNull String getQualifiedName();

  @NotNull String getName();

}
