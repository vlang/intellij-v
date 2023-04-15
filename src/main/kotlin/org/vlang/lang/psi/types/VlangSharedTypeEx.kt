package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.vlang.lang.psi.VlangNamedElement

class VlangSharedTypeEx(val inner: VlangTypeEx, anchor: PsiElement) : VlangResolvableTypeEx<VlangNamedElement>(anchor) {
    override fun toString() = "shared ".safeAppend(inner)

    override fun qualifiedName() = "shared ".safeAppend(inner.qualifiedName())

    override fun readableName(context: PsiElement) = "shared ".safeAppend(inner.readableName(context))

    override fun module() = inner.module()

    override fun isAssignableFrom(rhs: VlangTypeEx, project: Project): Boolean {
        if (rhs.isAny) return true
        if (rhs is VlangSharedTypeEx) {
            return inner.isAssignableFrom(rhs.inner, project)
        }
        return false
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

    override fun resolveImpl(project: Project): VlangNamedElement? {
        if (inner is VlangResolvableTypeEx<*>) {
            return inner.resolve(project)
        }

        return null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VlangSharedTypeEx

        return inner == other.inner
    }

    override fun hashCode(): Int = inner.hashCode() * 31
}
