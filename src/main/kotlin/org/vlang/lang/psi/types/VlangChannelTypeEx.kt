package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

class VlangChannelTypeEx(val inner: VlangTypeEx, anchor: PsiElement) : VlangBaseTypeEx(anchor) {
    override fun toString() = "chan ".safeAppend(inner)

    override fun qualifiedName() = "chan ".safeAppend(inner.qualifiedName())

    override fun readableName(context: PsiElement) = "chan ".safeAppend(inner.readableName(context))

    override fun module() = inner.module()

    override fun isAssignableFrom(rhs: VlangTypeEx, project: Project): Boolean {
        if (rhs.isAny) return true
        if (rhs is VlangChannelTypeEx) {
            return inner.isAssignableFrom(rhs.inner, project)
        }
        return false
    }

    override fun isEqual(rhs: VlangTypeEx): Boolean {
        return rhs is VlangChannelTypeEx && inner.isEqual(rhs.inner)
    }

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }

        inner.accept(visitor)
    }

    override fun substituteGenerics(nameMap: Map<String, VlangTypeEx>): VlangTypeEx {
        return VlangChannelTypeEx(inner.substituteGenerics(nameMap), anchor!!)
    }
}
