package org.vlang.lang.psi.types

import org.vlang.lang.psi.VlangCompositeElement
import org.vlang.lang.psi.VlangType

class VlangSimpleTypeEx(raw: VlangType): VlangBaseTypeEx<VlangType>(raw) {
    override fun toString(): String = raw.text

    override fun readableName(context: VlangCompositeElement): String = raw.text

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }
    }
}
