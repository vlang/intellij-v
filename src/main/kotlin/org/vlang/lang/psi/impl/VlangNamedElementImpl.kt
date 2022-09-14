package org.vlang.lang.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.ResolveState
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.psi.util.PsiTreeUtil
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.*
import org.vlang.lang.psi.VlangPsiTreeUtil.getChildOfType
import org.vlang.lang.stubs.VlangNamedStub

abstract class VlangNamedElementImpl<T : VlangNamedStub<*>> :
    VlangStubbedElementImpl<T>,
    VlangCompositeElement,
    VlangNamedElement {

    constructor(stub: T, nodeType: IStubElementType<*, *>) : super(stub, nodeType)
    constructor(node: ASTNode) : super(node)

    override fun isBlank(): Boolean = name == "_"

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

    override fun getSymbolVisibility(): VlangSymbolVisibility? {
        return getChildOfType(this, VlangSymbolVisibility::class.java)
    }

    override fun getReferences(): Array<PsiReference> {
        return ReferenceProvidersRegistry.getReferencesFromProviders(this)
    }

    override fun getName(): String? {
        val stub = stub
        if (stub != null) {
            return stub.name
        }
        val identifier = getIdentifier()
        return identifier?.text
    }

    override fun getQualifiedName(): String? {
        val name = name ?: return null
        val packageName = containingFile.packageName
        return VlangPsiImplUtil.getFqn(packageName, name)
    }

    override fun setName(name: String): PsiElement? {
        val identifier = getIdentifier()
        identifier?.replace(VlangElementFactory.createIdentifierFromText(project, name))
        return this
    }

    override fun getType(context: ResolveState?): VlangType? {
        if (context == null) {
            return null
        }

        val inner = getTypeInner(context)
        if (inner != null) {
            return inner
        }

        return CachedValuesManager.getCachedValue(this) {
            CachedValueProvider.Result
                .create(
                    getTypeInner(VlangPsiImplUtil.createContextOnElement(this)),
                    PsiModificationTracker.MODIFICATION_COUNT
                )
        }
    }

    protected open fun getTypeInner(context: ResolveState?) = findSiblingType()

    open fun findSiblingType(): VlangType? {
        val stub = stub
        return if (stub != null) {
            VlangPsiTreeUtil.getStubChildOfType(parentByStub, VlangType::class.java)
        } else
            PsiTreeUtil.getNextSiblingOfType(this, VlangType::class.java)
    }

    override fun getTextOffset(): Int {
        val identifier = getIdentifier()
        return identifier?.textOffset ?: super.getTextOffset()
    }

    override fun getNameIdentifier(): PsiElement? {
        return findChildByFilter(TokenSet.create(VlangTypes.IDENTIFIER))
    }
}
