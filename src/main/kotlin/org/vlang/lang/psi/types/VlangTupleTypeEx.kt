package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.vlang.lang.psi.VlangTupleType

class VlangTupleTypeEx(raw: VlangTupleType) : VlangBaseTypeEx<VlangTupleType>(raw) {
    private val types = raw.typeListNoPin.typeList.map { it.toEx() }

    override fun toString() = buildString {
        append("(")
        append(types.joinToString(", ") { it.toString() })
        append(")")
    }

    override fun qualifiedName() = buildString {
        append("(")
        append(types.joinToString(", ") { it.qualifiedName() })
        append(")")
    }

    override fun readableName(context: PsiElement) = buildString {
        append("(")
        append(types.joinToString(", ") { it.readableName(context) })
        append(")")
    }

    override fun isAssignableFrom(rhs: VlangTypeEx<*>, project: Project): Boolean {
        return true // TODO: implement this
    }

    override fun isEqual(rhs: VlangTypeEx<*>): Boolean {
        if (rhs !is VlangTupleTypeEx) return false

        if (types.size != rhs.types.size) {
            return false
        }

        return types.zip(rhs.types).all { (lhs, rhs) -> lhs.isEqual(rhs) }
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
