package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import org.vlang.lang.psi.VlangCompositeElement
import org.vlang.lang.psi.VlangNotNullableType

class VlangNotNullableTypeEx(raw: VlangNotNullableType) : VlangBaseTypeEx<VlangNotNullableType>(raw) {
    val inner = raw.type?.toEx()

    override fun toString() = inner.safeAppend("!")

    override fun readableName(context: VlangCompositeElement) = inner?.readableName(context).safeAppend("!")

    override fun isAssignableFrom(rhs: VlangTypeEx<*>, project: Project): Boolean {
        return when (rhs) {
            is VlangAnyTypeEx         -> true
            is VlangUnknownTypeEx     -> true
            is VlangVoidPtrTypeEx     -> true
            is VlangNotNullableTypeEx -> if (rhs.inner == null) true else inner?.isAssignableFrom(rhs.inner, project) ?: false
            else                      -> false
        }
    }

    override fun isEqual(rhs: VlangTypeEx<*>): Boolean {
        if (rhs !is VlangNotNullableTypeEx) {
            return false
        }

        if (rhs.inner == null && inner == null) {
            return true
        }

        return inner!!.isEqual(rhs.inner!!)
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