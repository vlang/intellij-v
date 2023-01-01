package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

class VlangStringTypeEx private constructor() : VlangStructTypeEx("string", null) {
    override val moduleName: String = "builtin"

    override fun toString() = "string"

    override fun qualifiedName(): String = "builtin.string"

    override fun readableName(context: PsiElement) = "string"

    override fun isAssignableFrom(rhs: VlangTypeEx, project: Project): Boolean {
        if (rhs.isAny) return true
        return rhs is VlangStringTypeEx
    }

    override fun isEqual(rhs: VlangTypeEx): Boolean = rhs is VlangStringTypeEx

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }
    }

    override fun substituteGenerics(nameMap: Map<String, VlangTypeEx>): VlangTypeEx = this

    companion object {
        val INSTANCE = VlangStringTypeEx()
    }
}
