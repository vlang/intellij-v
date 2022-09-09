package org.vlang.lang.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.VlangCompositeElement
import org.vlang.lang.psi.VlangNamedElement
import org.vlang.lang.stubs.VlangNamedStub

abstract class VlangNamedElementImpl<T : VlangNamedStub<*>> : VlangStubbedElementImpl<T>, VlangCompositeElement,
    VlangNamedElement {

    constructor(stub: T, nodeType: IStubElementType<*, *>) : super(stub, nodeType)
    constructor(node: ASTNode) : super(node)

    override fun isPublic(): Boolean {
        val stub = stub
        val isPublic = getSymbolVisibility()?.pub != null
        return stub?.isPublic ?: isPublic
    }

    override fun isGlobal(): Boolean {
        val stub = stub
        val isGlobal = getSymbolVisibility()?.builtinGlobal != null
        return stub?.isGlobal ?: isGlobal
    }

    override fun getReferences(): Array<PsiReference> {
        return ReferenceProvidersRegistry.getReferencesFromProviders(this)
    }

    override fun setName(name: String): PsiElement? {
        return null
    }

    override fun getTextOffset(): Int {
        val identifier = getIdentifier()
        return identifier?.textOffset ?: super.getTextOffset()
    }

    override fun getNameIdentifier(): PsiElement? {
//        var ident: PsiElement? = null
        val ident = this.findChildByFilter(TokenSet.create(VlangTypes.IDENTIFIER))
//        PsiTreeUtil.processElements(this) {
//            if (it.elementType == VlangTypes.IDENTIFIER) {
//                ident = it
//                return@processElements false
//            }
//
//            true
//        }
        return ident
    }
}