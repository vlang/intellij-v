package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.vlang.lang.psi.VlangSharedType

class VlangSharedTypeEx(raw: VlangSharedType) : VlangBaseTypeEx<VlangSharedType>(raw) {
    val inner = raw.type?.toEx()

    override fun toString() = "shared ".safeAppend(inner)

    override fun qualifiedName() = "shared ".safeAppend(inner?.qualifiedName())

    override fun readableName(context: PsiElement) = "shared ".safeAppend(inner?.readableName(context))

    override fun isAssignableFrom(rhs: VlangTypeEx<*>, project: Project): Boolean {
        return when (rhs) {
            is VlangAnyTypeEx     -> true
            is VlangUnknownTypeEx -> true
            is VlangVoidPtrTypeEx -> true
            is VlangSharedTypeEx  -> {
                val otherInner = rhs.inner
                if (otherInner == null) true else inner?.isAssignableFrom(otherInner, project) ?: false
            }

            else                  -> inner?.isAssignableFrom(rhs, project) ?: false
        }
    }

    override fun isEqual(rhs: VlangTypeEx<*>): Boolean {
        return rhs is VlangSharedTypeEx && rhs.inner?.let { inner?.isEqual(it) } ?: false
    }

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }

        if (inner != null) {
            visitor.enter(inner)
        }
    }
}
