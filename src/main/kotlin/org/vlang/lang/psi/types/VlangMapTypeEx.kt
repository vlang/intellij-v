package org.vlang.lang.psi.types

import org.vlang.lang.psi.VlangCompositeElement
import org.vlang.lang.psi.VlangMapType

class VlangMapTypeEx(raw: VlangMapType): VlangBaseTypeEx<VlangMapType>(raw) {
    private val key = raw.keyType?.toEx()
    private val value = raw.valueType?.toEx()

    override fun toString() = buildString {
        append("map[")
        key?.let { append(it) }
        append("]")
        value?.let { append(it) }
    }

    override fun readableName(context: VlangCompositeElement) = buildString {
        append("map[")
        key?.let { append(it.readableName(context)) }
        append("]")
        value?.let { append(it.readableName(context)) }
    }

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }

        if (key != null) {
            if (!visitor.enter(key)) {
                return
            }
        }

        if (value != null) {
            if (!visitor.enter(value)) {
                return
            }
        }
    }
}
