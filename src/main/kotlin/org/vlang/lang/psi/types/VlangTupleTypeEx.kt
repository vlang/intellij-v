package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

class VlangTupleTypeEx(val types: List<VlangTypeEx>, anchor: PsiElement) : VlangBaseTypeEx(anchor) {
    override fun toString() = buildString {
        append("(")
        append(types.joinToString(", ") { it.toString() })
        append(")")
    }

    override fun qualifiedName(): String = buildString {
        append("(")
        append(types.joinToString(", ") { it.qualifiedName() })
        append(")")
    }

    override fun readableName(context: PsiElement) = buildString {
        append("(")
        append(types.joinToString(", ") { it.readableName(context) })
        append(")")
    }

    override fun module() = types.firstOrNull()?.module() ?: super.module()

    override fun isAssignableFrom(rhs: VlangTypeEx, project: Project): Boolean {
        return false
    }

    override fun isEqual(rhs: VlangTypeEx): Boolean {
        return rhs is VlangTupleTypeEx && types.size == rhs.types.size && types.zip(rhs.types).all { (lhs, rhs) -> lhs.isEqual(rhs) }
    }

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }

        for (type in types) {
            type.accept(visitor)
        }
    }

    override fun substituteGenerics(nameMap: Map<String, VlangTypeEx>): VlangTypeEx {
        return VlangTupleTypeEx(types.map { it.substituteGenerics(nameMap) }, anchor!!)
    }
}
