package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

class VlangNoneTypeEx private constructor(): VlangBaseTypeEx() {
    override fun toString(): String = "none"

    override fun qualifiedName(): String  = "none"

    override fun readableName(context: PsiElement): String = "none"

    override fun isAssignableFrom(rhs: VlangTypeEx, project: Project): Boolean = rhs is VlangNoneTypeEx

    override fun isEqual(rhs: VlangTypeEx): Boolean = rhs is VlangNoneTypeEx

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }
    }

    override fun substituteGenerics(nameMap: Map<String, VlangTypeEx>): VlangTypeEx = this

    companion object {
        val INSTANCE = VlangNoneTypeEx()
    }
}
