package org.vlang.lang.psi

import com.intellij.navigation.NavigationItem
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner

interface VlangNamedElement : VlangCompositeElement, PsiNameIdentifierOwner, NavigationItem {
    fun getIdentifier(): PsiElement?
}

