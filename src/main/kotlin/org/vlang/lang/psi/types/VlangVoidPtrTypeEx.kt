package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.vlang.lang.psi.VlangType

class VlangVoidPtrTypeEx(raw: VlangType) : VlangBaseTypeEx<VlangType>(raw) {
    override fun toString(): String = "voidptr"

    override fun qualifiedName() = "voidptr"

    override fun readableName(context: PsiElement): String = "voidptr"

    override fun isAssignableFrom(rhs: VlangTypeEx<*>, project: Project) = true

    override fun isEqual(rhs: VlangTypeEx<*>): Boolean {
        return rhs is VlangVoidPtrTypeEx
    }

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }
    }
}
