package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import org.vlang.lang.psi.VlangArrayOrSliceType
import org.vlang.lang.psi.VlangCompositeElement

class VlangArrayTypeEx(raw: VlangArrayOrSliceType) : VlangBaseTypeEx<VlangArrayOrSliceType>(raw) {
    val inner = raw.type?.toEx()

    override fun toString() = "[]".safeAppend(inner)

    override fun readableName(context: VlangCompositeElement) = "[]".safeAppend(inner?.readableName(context))

    override fun isAssignableFrom(rhs: VlangTypeEx<*>, project: Project): Boolean {
        return when (rhs) {
            is VlangAnyTypeEx             -> true
            is VlangVoidPtrTypeEx         -> true
            is VlangUnknownTypeEx      -> true
            is VlangBuiltinArrayTypeEx -> true
            is VlangArrayTypeEx        -> {
                val otherInner = rhs.inner
                if (otherInner == null) true else inner?.isAssignableFrom(otherInner, project) ?: false
            }

            else                          -> false
        }
    }

    override fun isEqual(rhs: VlangTypeEx<*>): Boolean {
        return rhs is VlangArrayTypeEx && rhs.inner?.let { inner?.isEqual(it) } ?: false
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
