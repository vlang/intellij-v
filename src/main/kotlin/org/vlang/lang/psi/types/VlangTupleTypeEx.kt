package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import org.vlang.lang.psi.VlangCompositeElement
import org.vlang.lang.psi.VlangTupleType

class VlangTupleTypeEx(raw: VlangTupleType) : VlangBaseTypeEx<VlangTupleType>(raw) {
    private val types = raw.typeListNoPin.typeList.map { it.toEx() }

    override fun toString() = buildString {
        append("(")
        append(types.joinToString(", ") { it.toString() })
        append(")")
    }

    override fun readableName(context: VlangCompositeElement) = buildString {
        append("(")
        append(types.joinToString(", ") { it.readableName(context) })
        append(")")
    }

    override fun isAssignableFrom(rhs: VlangTypeEx<*>, project: Project): Boolean {
        return true // TODO: implement this
    }

    override fun isEqual(rhs: VlangTypeEx<*>): Boolean {
        return true // TODO: implement this
    }

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }

        for (type in types) {
            if (!visitor.enter(type)) {
                return
            }
        }
    }
}
