package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.vlang.lang.psi.VlangMapType

class VlangMapTypeEx(raw: VlangMapType): VlangBaseTypeEx<VlangMapType>(raw) {
    val key = raw.keyType.toEx()
    val value = raw.valueType.toEx()

    override fun toString() = buildString {
        append("map[")
        append(key)
        append("]")
        append(value)
    }

    override fun qualifiedName() = buildString {
        append("map[")
        append(key.qualifiedName())
        append("]")
        append(value.qualifiedName())
    }

    override fun readableName(context: PsiElement) = buildString {
        append("map[")
        append(key.readableName(context))
        append("]")
        append(value.readableName(context))
    }

    override fun isAssignableFrom(rhs: VlangTypeEx<*>, project: Project): Boolean {
        return true // TODO: implement this
    }

    override fun isEqual(rhs: VlangTypeEx<*>): Boolean {
        if (rhs !is VlangMapTypeEx) return false

        return key.isEqual(rhs.key) && value.isEqual(rhs.value)
    }

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }

        if (!visitor.enter(key)) {
            return
        }

        if (!visitor.enter(value)) {
            return
        }
    }
}
