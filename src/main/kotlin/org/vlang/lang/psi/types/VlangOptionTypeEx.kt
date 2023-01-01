package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

class VlangOptionTypeEx(val inner: VlangTypeEx?, anchor: PsiElement) : VlangBaseTypeEx(anchor) {
    override fun toString() = "?".safeAppend(inner)

    override fun qualifiedName(): String = "?".safeAppend(inner?.qualifiedName())

    override fun readableName(context: PsiElement) = "?".safeAppend(inner?.readableName(context))

    override fun module() = inner?.module() ?: super.module()

    override fun isAssignableFrom(rhs: VlangTypeEx, project: Project): Boolean {
        if (rhs.isAny) return true
        if (rhs is VlangOptionTypeEx) {
            return rhs.inner?.let { inner?.isAssignableFrom(it, project) } ?: false
        }
        return false
    }

    override fun isEqual(rhs: VlangTypeEx): Boolean {
        if (rhs !is VlangOptionTypeEx) {
            return false
        }

        if (inner == null && rhs.inner == null) {
            return true
        }

        if (inner == null || rhs.inner == null) {
            return false
        }

        return inner.isEqual(rhs.inner)
    }

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }

        inner?.accept(visitor)
    }

    override fun substituteGenerics(nameMap: Map<String, VlangTypeEx>): VlangTypeEx {
        return VlangOptionTypeEx(inner?.substituteGenerics(nameMap), anchor!!)
    }
}
