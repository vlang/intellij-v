package org.vlang.lang.psi

import com.intellij.navigation.NavigationItem
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner

interface VlangNamedElement : VlangCompositeElement, PsiNameIdentifierOwner, NavigationItem {
    fun isPublic(): Boolean
    fun isGlobal(): Boolean
    fun getIdentifier(): PsiElement?
    fun getSymbolVisibility(): VlangSymbolVisibility?
}
