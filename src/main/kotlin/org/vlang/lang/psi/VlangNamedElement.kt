package org.vlang.lang.psi

import com.intellij.navigation.NavigationItem
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner

interface VlangNamedElement : VlangTypeOwner, VlangVisibilityOwner, VlangDocumentationOwner, VlangCompositeElement, PsiNameIdentifierOwner, NavigationItem {
    fun isBlank(): Boolean
    fun isGlobal(): Boolean
    fun getIdentifier(): PsiElement?
    fun getQualifiedName(): String?
    fun getOwner(): PsiElement?
    fun isDeprecated(): Boolean
    fun getModuleName(): String?
    fun isCaptured(context: PsiElement): Boolean
    fun getCapturePlace(context: PsiElement): VlangCapture?
}
