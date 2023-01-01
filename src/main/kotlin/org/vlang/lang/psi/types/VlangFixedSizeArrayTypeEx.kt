package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

class VlangFixedSizeArrayTypeEx(inner: VlangTypeEx, val size: Int, anchor: PsiElement) : VlangArrayTypeEx(inner, anchor) {
    override fun toString() = "[$size]".safeAppend(inner)

    override fun qualifiedName(): String = "[$size]".safeAppend(inner.qualifiedName())

    override fun readableName(context: PsiElement) = "[$size]".safeAppend(inner.readableName(context))

    override fun module() = inner.module()

    override fun isAssignableFrom(rhs: VlangTypeEx, project: Project): Boolean {
        if (rhs.isAny) return true

        return when (rhs) {
            is VlangBuiltinArrayTypeEx   -> true
            is VlangFixedSizeArrayTypeEx -> size == rhs.size && inner.isAssignableFrom(rhs.inner, project)
            else                         -> false
        }
    }

    override fun isEqual(rhs: VlangTypeEx): Boolean {
        return rhs is VlangFixedSizeArrayTypeEx && size == rhs.size && inner.isEqual(rhs.inner)
    }

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }

        inner.accept(visitor)
    }

    override fun substituteGenerics(nameMap: Map<String, VlangTypeEx>): VlangTypeEx {
        return VlangFixedSizeArrayTypeEx(inner.substituteGenerics(nameMap), size, anchor!!)
    }
}
