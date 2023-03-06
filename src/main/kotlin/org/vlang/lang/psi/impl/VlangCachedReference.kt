package org.vlang.lang.psi.impl

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.impl.source.resolve.ResolveCache
import com.intellij.util.ArrayUtil

abstract class VlangCachedReference<T : PsiElement>(element: T, range: TextRange = TextRange.from(0, element.textLength)) :
    PsiReferenceBase<T>(element, range) {

    protected val project = element.project

    override fun resolve(): PsiElement? {
        if (!myElement!!.isValid) {
            return null
        }

        return ResolveCache.getInstance(myElement!!.project)
            .resolveWithCaching(this, MY_RESOLVER, false, false)
    }

    protected abstract fun resolveInner(): PsiElement?

    abstract fun processResolveVariants(processor: VlangScopeProcessor): Boolean

    override fun handleElementRename(newElementName: String): PsiElement {
        return myElement!!.replace(VlangElementFactory.createIdentifier(myElement!!.project, newElementName))
    }

    override fun getVariants(): Array<Any> = ArrayUtil.EMPTY_OBJECT_ARRAY

    override fun equals(other: Any?) = this === other || other is VlangCachedReference<*> && element === other.element

    override fun hashCode() = element.hashCode()

    companion object {
        private val MY_RESOLVER =
            ResolveCache.AbstractResolver { r: VlangCachedReference<*>, _ -> r.resolveInner() }
    }
}
