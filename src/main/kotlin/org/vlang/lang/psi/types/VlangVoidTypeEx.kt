package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import org.vlang.lang.psi.VlangCompositeElement
import org.vlang.lang.psi.VlangType

class VlangVoidTypeEx(raw: VlangType) : VlangBaseTypeEx<VlangType>(raw) {
    override fun toString(): String = "void"

    override fun readableName(context: VlangCompositeElement): String = "void"

    override fun isAssignableFrom(rhs: VlangTypeEx<*>, project: Project): Boolean {
        return when (rhs) {
            is VlangAnyTypeEx     -> true
            is VlangUnknownTypeEx -> true
            is VlangVoidPtrTypeEx -> true
            else                  -> false
        }
    }

    override fun isEqual(rhs: VlangTypeEx<*>): Boolean {
        return rhs is VlangVoidTypeEx
    }

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }
    }
}
