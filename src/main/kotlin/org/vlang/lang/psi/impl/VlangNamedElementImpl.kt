package org.vlang.lang.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.VlangNamedElement

abstract class VlangNamedElementImpl(node: ASTNode) : ASTWrapperPsiElement(node), VlangNamedElement {
    override fun shouldGoDeeper(): Boolean {
        return false
    }

    override fun getReferences(): Array<PsiReference> {
        return ReferenceProvidersRegistry.getReferencesFromProviders(this);
    }

    override fun setName(name: String): PsiElement? {
        return null
    }

    override fun getTextOffset(): Int {
        val identifier = getIdentifier()
        return identifier?.textOffset ?: super.getTextOffset()
    }

    override fun getNameIdentifier(): PsiElement? {
        return findNotNullChildByType(VlangTypes.IDENTIFIER)
    }
}
