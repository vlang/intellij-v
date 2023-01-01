package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

class VlangBuiltinArrayTypeEx private constructor() : VlangStructTypeEx("array", null) {
    override val moduleName: String = "builtin"

    override fun toString() = "array"

    override fun qualifiedName(): String  = "builtin.array"

    override fun readableName(context: PsiElement) = "array"

    override fun isAssignableFrom(rhs: VlangTypeEx, project: Project): Boolean {
        if (rhs.isAny) return true

        return when (rhs) {
            is VlangBuiltinArrayTypeEx -> true
            is VlangArrayTypeEx        -> true
            else                       -> false
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
        val INSTANCE = VlangBuiltinArrayTypeEx()
    }
}
