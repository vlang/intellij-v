package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import org.vlang.lang.psi.VlangCompositeElement
import org.vlang.lang.psi.VlangType

class VlangVoidPtrTypeEx(raw: VlangType) : VlangBaseTypeEx<VlangType>(raw) {
    override fun toString(): String = "voidptr"

    override fun readableName(context: VlangCompositeElement): String = "voidptr"

    override fun isAssignableFrom(rhs: VlangTypeEx<*>, project: Project) = true

    override fun isEqual(rhs: VlangTypeEx<*>): Boolean {
        return rhs is VlangVoidPtrTypeEx
    }

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }
    }
}
