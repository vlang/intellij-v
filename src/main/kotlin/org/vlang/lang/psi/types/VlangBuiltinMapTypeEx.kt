package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

class VlangBuiltinMapTypeEx private constructor() : VlangStructTypeEx("map", null) {
    override val moduleName: String = "builtin"

    override fun toString() = "map"

    override fun qualifiedName(): String = "builtin.map"

    override fun readableName(context: PsiElement) = "map"

    override fun isAssignableFrom(rhs: VlangTypeEx, project: Project): Boolean {
        if (rhs.isAny) return true

        return when (rhs) {
            is VlangBuiltinMapTypeEx -> true
            is VlangMapTypeEx        -> true
            else                     -> false
        }
    }

    override fun isEqual(rhs: VlangTypeEx): Boolean = true

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }
    }

    override fun substituteGenerics(nameMap: Map<String, VlangTypeEx>): VlangTypeEx = this

    companion object {
        val INSTANCE = VlangBuiltinMapTypeEx()
    }
}
