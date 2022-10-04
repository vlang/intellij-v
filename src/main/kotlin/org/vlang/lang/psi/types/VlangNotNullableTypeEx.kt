package org.vlang.lang.psi.types

import org.vlang.lang.psi.VlangCompositeElement
import org.vlang.lang.psi.VlangNotNullableType

class VlangNotNullableTypeEx(raw: VlangNotNullableType): VlangBaseTypeEx<VlangNotNullableType>(raw) {
    private val inner = raw.type?.toEx()

    override fun toString() = inner.safeAppend("!")

    override fun readableName(context: VlangCompositeElement) = inner?.readableName(context).safeAppend("!")

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }

        if (inner != null) {
            visitor.enter(inner)
        }
    }
}
