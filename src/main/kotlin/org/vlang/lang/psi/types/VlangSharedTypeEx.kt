package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

class VlangSharedTypeEx(val inner: VlangTypeEx, anchor: PsiElement) : VlangBaseTypeEx(anchor) {
    override fun toString() = "shared ".safeAppend(inner)

    override fun qualifiedName() = "shared ".safeAppend(inner.qualifiedName())

    override fun readableName(context: PsiElement) = "shared ".safeAppend(inner.readableName(context))

    override fun module() = inner.module()

    override fun isAssignableFrom(rhs: VlangTypeEx, project: Project): Boolean {
        return when (rhs) {
            is VlangAnyTypeEx     -> true
            is VlangUnknownTypeEx -> true
            is VlangVoidPtrTypeEx -> true
            is VlangSharedTypeEx  -> inner.isAssignableFrom(rhs.inner, project)
            else                  -> inner.isAssignableFrom(rhs, project)
        }
    }

    override fun isEqual(rhs: VlangTypeEx): Boolean {
        return rhs is VlangSharedTypeEx && inner.isEqual(rhs.inner)
    }

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }

        inner.accept(visitor)
    }

    override fun substituteGenerics(nameMap: Map<String, VlangTypeEx>): VlangTypeEx {
        return VlangSharedTypeEx(inner.substituteGenerics(nameMap), anchor!!)
    }
}
