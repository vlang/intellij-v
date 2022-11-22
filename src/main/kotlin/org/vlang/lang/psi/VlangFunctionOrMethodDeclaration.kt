package org.vlang.lang.psi

import com.intellij.psi.PsiElement

interface VlangFunctionOrMethodDeclaration : VlangAttributeOwner, VlangNamedElement, VlangGenericParametersOwner {
    fun getBlock(): VlangBlock?
    fun getSignature(): VlangSignature?
    fun getFn(): PsiElement
    override fun getName(): String?
    override fun getSymbolVisibility(): VlangSymbolVisibility?
}
