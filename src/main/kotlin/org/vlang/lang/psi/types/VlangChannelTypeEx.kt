package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.vlang.lang.psi.VlangChannelType

class VlangChannelTypeEx(raw: VlangChannelType) : VlangBaseTypeEx<VlangChannelType>(raw) {
    val inner = raw.type?.toEx()

    override fun toString() = "chan ".safeAppend(inner)

    override fun qualifiedName() = "chan ".safeAppend(inner?.qualifiedName())

    override fun readableName(context: PsiElement) = "chan ".safeAppend(inner?.readableName(context))

    override fun isAssignableFrom(rhs: VlangTypeEx<*>, project: Project): Boolean {
        return when (rhs) {
            is VlangAnyTypeEx     -> true
            is VlangUnknownTypeEx -> true
            is VlangVoidPtrTypeEx -> true
            is VlangChannelTypeEx -> {
                val otherInner = rhs.inner
                if (otherInner == null) true else inner?.isAssignableFrom(otherInner, project) ?: false
            }

            else                  -> inner?.isAssignableFrom(rhs, project) ?: false
        }
    }

    override fun isEqual(rhs: VlangTypeEx<*>): Boolean {
        return rhs is VlangChannelTypeEx && rhs.inner?.let { inner?.isEqual(it) } ?: false
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
