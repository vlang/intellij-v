package org.vlang.lang.psi

import com.intellij.navigation.NavigationItem
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner

interface VlangNamedElement : VlangTypeOwner, VlangCompositeElement, PsiNameIdentifierOwner, NavigationItem {
    fun isBlank(): Boolean
    fun isPublic(): Boolean
    fun isGlobal(): Boolean
    fun getIdentifier(): PsiElement?
    fun getQualifiedName(): String?
    fun getSymbolVisibility(): VlangSymbolVisibility?
    fun getOwner(): PsiElement?
}
