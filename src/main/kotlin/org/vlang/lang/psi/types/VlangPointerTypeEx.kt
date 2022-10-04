package org.vlang.lang.psi.types

import org.vlang.lang.psi.VlangCompositeElement
import org.vlang.lang.psi.VlangPointerType

class VlangPointerTypeEx(raw: VlangPointerType): VlangBaseTypeEx<VlangPointerType>(raw) {
    private val inner = raw.type?.toEx()

    override fun toString() = "*".safeAppend(inner)

    override fun readableName(context: VlangCompositeElement) = "*".safeAppend(inner?.readableName(context))

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }

        if (inner != null) {
            visitor.enter(inner)
        }
    }
}
