package org.vlang.lang.psi

import com.intellij.psi.PsiElement

interface VlangFunctionOrMethodDeclaration : /*: VlangTopLevelDeclaration, VlangNamedSignatureOwner*/ VlangNamedElement {
    fun getBlock(): VlangBlock?

    fun getSignature(): VlangSignature?

    fun getFn(): PsiElement
}
