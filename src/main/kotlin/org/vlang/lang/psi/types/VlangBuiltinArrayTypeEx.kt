package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import org.vlang.lang.psi.VlangCompositeElement
import org.vlang.lang.psi.VlangStructType
import org.vlang.lang.psi.VlangType

class VlangBuiltinArrayTypeEx(raw: VlangStructType) : VlangBaseTypeEx<VlangType>(raw) {
    override fun toString() = "array"

    override fun readableName(context: VlangCompositeElement) = "array"

    override fun isAssignableFrom(rhs: VlangTypeEx<*>, project: Project): Boolean {
        return when (rhs) {
            is VlangAnyTypeEx          -> true
            is VlangUnknownTypeEx      -> true
            is VlangVoidPtrTypeEx      -> true
            is VlangBuiltinArrayTypeEx -> true
            is VlangArrayTypeEx        -> true
            else                          -> false
        }
    }

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }
    }
}
