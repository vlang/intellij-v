package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

open class VlangArrayTypeEx(val inner: VlangTypeEx, anchor: PsiElement) : VlangBaseTypeEx(anchor) {
    override fun toString() = "[]".safeAppend(inner)

    override fun qualifiedName(): String  = "[]".safeAppend(inner.qualifiedName())

    override fun readableName(context: PsiElement) = "[]".safeAppend(inner.readableName(context))

    override fun module() = inner.module()

    override fun isAssignableFrom(rhs: VlangTypeEx, project: Project): Boolean {
        if (rhs.isAny) return true

        return when (rhs) {
            is VlangBuiltinArrayTypeEx -> true
            is VlangArrayTypeEx        -> inner.isAssignableFrom(rhs.inner, project)
            else                       -> false
        }
    }

    override fun isEqual(rhs: VlangTypeEx): Boolean {
        return rhs is VlangArrayTypeEx && inner.isEqual(rhs.inner)
    }

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }

        inner.accept(visitor)
    }

    override fun substituteGenerics(nameMap: Map<String, VlangTypeEx>): VlangTypeEx {
        return VlangArrayTypeEx(inner.substituteGenerics(nameMap), anchor!!)
    }
}
