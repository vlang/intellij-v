package org.vlang.lang.psi.types

import org.vlang.lang.psi.VlangCompositeElement
import org.vlang.lang.psi.VlangType

class VlangPrimitiveTypeEx(raw: VlangType, private val name: VlangPrimitiveTypes): VlangBaseTypeEx<VlangType>(raw) {
    override fun toString(): String = name.value

    override fun readableName(context: VlangCompositeElement): String = name.value

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }
    }
}
