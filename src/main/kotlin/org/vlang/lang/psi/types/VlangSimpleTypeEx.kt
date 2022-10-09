package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import org.vlang.lang.psi.VlangCompositeElement
import org.vlang.lang.psi.VlangType

class VlangSimpleTypeEx(raw: VlangType) : VlangBaseTypeEx<VlangType>(raw) {
    override fun toString(): String = raw.text

    override fun readableName(context: VlangCompositeElement): String = raw.text

    override fun isAssignableFrom(rhs: VlangTypeEx<*>, project: Project): Boolean {
        return when (rhs) {
            is VlangAnyTypeEx     -> true
            is VlangUnknownTypeEx -> true
            is VlangVoidPtrTypeEx -> true
            is VlangSimpleTypeEx  -> raw.text == rhs.raw.text
            else                  -> false
        }
    }

    override fun isEqual(rhs: VlangTypeEx<*>): Boolean {
        return rhs is VlangSimpleTypeEx && raw.text == rhs.raw().text
    }

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }
    }
}
