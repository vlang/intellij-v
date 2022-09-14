package org.vlang.lang.psi.impl

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.impl.source.resolve.ResolveCache
import com.intellij.util.ArrayUtil

abstract class VlangCachedReference<T : PsiElement>(element: T) : PsiReferenceBase<T>(element, TextRange.from(0, element.textLength)) {

    protected abstract fun resolveInner(): PsiElement?

    override fun resolve(): PsiElement? {
        return if (myElement!!.isValid)
            ResolveCache.getInstance(myElement!!.project)
                .resolveWithCaching(this, MY_RESOLVER, false, false)
        else
            null
    }

    abstract fun processResolveVariants(processor: VlangScopeProcessor): Boolean

    override fun handleElementRename(newElementName: String): PsiElement {
        myElement!!.replace(VlangElementFactory.createIdentifierFromText(myElement!!.project, newElementName))
        return myElement
    }

    override fun getVariants(): Array<Any> = ArrayUtil.EMPTY_OBJECT_ARRAY

    override fun equals(o: Any?) = this === o || o is VlangCachedReference<*> && element === o.element

    override fun hashCode() = element.hashCode()

    companion object {
        private val MY_RESOLVER = ResolveCache.AbstractResolver { r: VlangCachedReference<*>, b: Boolean -> r.resolveInner() }
    }
}
