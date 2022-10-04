package org.vlang.lang.psi.types

import org.vlang.lang.psi.VlangCompositeElement
import org.vlang.lang.psi.VlangMapType

class VlangMapTypeEx(raw: VlangMapType): VlangBaseTypeEx<VlangMapType>(raw) {
    private val key = raw.keyType.toEx()
    private val value = raw.valueType.toEx()

    override fun toString() = buildString {
        append("map[")
        append(key)
        append("]")
        append(value)
    }

    override fun readableName(context: VlangCompositeElement) = buildString {
        append("map[")
        append(key.readableName(context))
        append("]")
        append(value.readableName(context))
    }

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }

        if (!visitor.enter(key)) {
            return
        }

        if (!visitor.enter(value)) {
            return
        }
    }
}
