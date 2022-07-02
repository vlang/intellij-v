package org.vlang.lang.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry
import com.intellij.psi.stubs.IStubElementType
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.VlangCompositeElement
import org.vlang.lang.psi.VlangNamedElement
import org.vlang.lang.stubs.VlangNamedStub

abstract class VlangNamedElementImpl<T : VlangNamedStub<*>> : VlangStubbedElementImpl<T>, VlangCompositeElement,
    VlangNamedElement {

    constructor(stub: T, nodeType: IStubElementType<*, *>) : super(stub, nodeType) {}
    constructor(node: ASTNode) : super(node) {}

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