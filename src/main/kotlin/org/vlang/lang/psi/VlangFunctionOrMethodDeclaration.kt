package org.vlang.lang.psi

import com.intellij.psi.PsiElement

interface VlangFunctionOrMethodDeclaration : /*: VlangTopLevelDeclaration, VlangNamedSignatureOwner*/ VlangNamedElement {
    fun getAttributes(): VlangAttributes?
    fun getBlock(): VlangBlock?
    fun getSignature(): VlangSignature?
    fun getFn(): PsiElement
    override fun getName(): String?
    override fun getSymbolVisibility(): VlangSymbolVisibility?
}
