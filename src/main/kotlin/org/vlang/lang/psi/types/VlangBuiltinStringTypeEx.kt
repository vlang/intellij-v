package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.vlang.lang.psi.VlangStructType
import org.vlang.lang.psi.VlangType

class VlangBuiltinStringTypeEx(raw: VlangStructType) : VlangBaseTypeEx<VlangType>(raw) {
    override fun toString() = "string"

    override fun qualifiedName(): String = "builtin.string"

    override fun readableName(context: PsiElement) = "string"

    override fun isAssignableFrom(rhs: VlangTypeEx<*>, project: Project): Boolean {
        return when (rhs) {
            is VlangAnyTypeEx           -> true
            is VlangUnknownTypeEx       -> true
            is VlangVoidPtrTypeEx       -> true
            is VlangBuiltinStringTypeEx -> true
            else                        -> false
        }
    }

    override fun isEqual(rhs: VlangTypeEx<*>): Boolean = rhs is VlangBuiltinStringTypeEx

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }
    }
}
