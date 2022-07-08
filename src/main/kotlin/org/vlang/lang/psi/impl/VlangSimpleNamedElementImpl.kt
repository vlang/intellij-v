package org.vlang.lang.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import org.vlang.lang.psi.VlangNamedElement
import org.vlang.lang.psi.VlangSymbolVisibility

abstract class VlangSimpleNamedElementImpl(node: ASTNode) : ASTWrapperPsiElement(node), VlangNamedElement {
    override fun getNameIdentifier(): PsiElement? = null

    override fun getName(): String? = null

    override fun setName(name: String) = this

    override fun getSymbolVisibility(): VlangSymbolVisibility? = null

    override fun isPublic() = true

    override fun isGlobal() = false
}
