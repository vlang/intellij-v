package org.vlang.lang.psi.types

import org.vlang.lang.psi.VlangCompositeElement
import org.vlang.lang.psi.VlangType

class VlangUnknownTypeEx(raw: VlangType?): VlangBaseTypeEx<VlangType?>(raw) {
    override fun toString(): String = VlangPrimitiveTypes.ANY.value

    override fun readableName(context: VlangCompositeElement): String = VlangPrimitiveTypes.ANY.value

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }
    }
}
