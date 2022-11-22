package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

class VlangUnknownTypeEx private constructor() : VlangBaseTypeEx(null) {
    override fun toString(): String = "any"

    override fun qualifiedName(): String = "any"

    override fun readableName(context: PsiElement): String = "any"

    override fun isAssignableFrom(rhs: VlangTypeEx, project: Project): Boolean = true

    override fun isEqual(rhs: VlangTypeEx): Boolean = true

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }
    }

    override fun substituteGenerics(nameMap: Map<String, VlangTypeEx>): VlangTypeEx = this

    companion object {
        val INSTANCE = VlangUnknownTypeEx()
    }
}
