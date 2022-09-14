package org.vlang.lang.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import org.vlang.lang.psi.VlangNamedElement
import org.vlang.lang.psi.VlangSymbolVisibility
import org.vlang.lang.psi.VlangType

abstract class VlangSimpleNamedElementImpl(node: ASTNode) : ASTWrapperPsiElement(node), VlangNamedElement {
    override fun getNameIdentifier(): PsiElement? = null

    override fun getQualifiedName(): String? = name

    override fun getName(): String? = null

    override fun setName(name: String) = this

    override fun getSymbolVisibility(): VlangSymbolVisibility? = null

    override fun isBlank(): Boolean = name == "_"

    override fun isPublic() = true

    override fun isGlobal() = false

    override fun getType(context: ResolveState?): VlangType? = null
}
