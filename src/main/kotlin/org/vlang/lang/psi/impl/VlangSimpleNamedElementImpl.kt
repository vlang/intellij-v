package org.vlang.lang.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import org.vlang.lang.psi.VlangNamedElement

abstract class VlangSimpleNamedElementImpl(node: ASTNode) : ASTWrapperPsiElement(node), VlangNamedElement {
    override fun getNameIdentifier(): PsiElement? {
        return null
    }

    override fun getName(): String? {
        return null
    }

    override fun setName(name: String): PsiElement {
        return this
    }
}