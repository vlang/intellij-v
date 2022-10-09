package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import org.vlang.lang.psi.VlangCompositeElement
import org.vlang.lang.psi.VlangType

class VlangAnyTypeEx(raw: VlangType): VlangBaseTypeEx<VlangType>(raw) {
    override fun toString(): String = "any"

    override fun readableName(context: VlangCompositeElement): String = "any"

    override fun isAssignableFrom(rhs: VlangTypeEx<*>, project: Project): Boolean = true

    override fun isEqual(rhs: VlangTypeEx<*>): Boolean = true

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }
    }
}
