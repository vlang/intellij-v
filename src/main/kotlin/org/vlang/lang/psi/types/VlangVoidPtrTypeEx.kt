package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

class VlangVoidPtrTypeEx private constructor() : VlangBaseTypeEx() {
    override fun toString(): String = "voidptr"

    override fun qualifiedName(): String  = "voidptr"

    override fun readableName(context: PsiElement): String = "voidptr"

    override fun isAssignableFrom(rhs: VlangTypeEx, project: Project) = true

    override fun isEqual(rhs: VlangTypeEx): Boolean {
        return rhs is VlangVoidPtrTypeEx
    }

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }
    }

    override fun substituteGenerics(nameMap: Map<String, VlangTypeEx>): VlangTypeEx = this

    companion object {
        val INSTANCE = VlangVoidPtrTypeEx()
    }
}
