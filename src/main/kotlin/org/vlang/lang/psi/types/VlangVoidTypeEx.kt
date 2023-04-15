package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

class VlangVoidTypeEx private constructor() : VlangBaseTypeEx() {
    override fun toString(): String = "void"

    override fun qualifiedName(): String  = "void"

    override fun readableName(context: PsiElement): String = "void"

    override fun isAssignableFrom(rhs: VlangTypeEx, project: Project): Boolean {
        return rhs.isAny
    }

    override fun isEqual(rhs: VlangTypeEx): Boolean {
        return rhs is VlangVoidTypeEx
    }

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }
    }

    override fun substituteGenerics(nameMap: Map<String, VlangTypeEx>): VlangTypeEx = this

    companion object {
        val INSTANCE = VlangVoidTypeEx()
    }
}
