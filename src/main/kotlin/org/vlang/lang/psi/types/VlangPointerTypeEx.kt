package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import org.vlang.lang.psi.VlangCompositeElement
import org.vlang.lang.psi.VlangPointerType

class VlangPointerTypeEx(raw: VlangPointerType) : VlangBaseTypeEx<VlangPointerType>(raw) {
    val inner = raw.type?.toEx()

    override fun toString() = "&".safeAppend(inner)

    override fun readableName(context: VlangCompositeElement) = "&".safeAppend(inner?.readableName(context))

    override fun isAssignableFrom(rhs: VlangTypeEx<*>, project: Project): Boolean {
        return when (rhs) {
            is VlangAnyTypeEx     -> true
            is VlangUnknownTypeEx -> true
            is VlangVoidPtrTypeEx -> true
            is VlangPointerTypeEx -> {
                val otherInner = rhs.inner
                if (otherInner == null) true else inner?.isAssignableFrom(otherInner, project) ?: false
            }

            else                  -> inner?.isAssignableFrom(rhs, project) ?: false
        }
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
