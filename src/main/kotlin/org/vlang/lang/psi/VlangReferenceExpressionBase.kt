package org.vlang.lang.psi

import com.intellij.psi.PsiElement

interface VlangReferenceExpressionBase : VlangCompositeElement {
    fun getIdentifier(): PsiElement?
    fun getQualifier(): VlangCompositeElement?
    fun resolve(): PsiElement?
}
