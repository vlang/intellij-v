package org.vlang.lang.psi.types

import org.vlang.lang.psi.VlangCompositeElement
import org.vlang.lang.psi.VlangType

interface VlangTypeEx<T: VlangType> {
    fun readableName(context: VlangCompositeElement): String
    fun module(): String
    fun raw(): T
    fun accept(visitor: VlangTypeVisitor)
}
