package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.vlang.lang.psi.VlangStructType
import org.vlang.lang.psi.VlangType

class VlangBuiltinArrayTypeEx(raw: VlangStructType) : VlangBaseTypeEx<VlangType>(raw) {
    override fun toString() = "array"

    override fun readableName(context: PsiElement) = "array"

    override fun isAssignableFrom(rhs: VlangTypeEx<*>, project: Project): Boolean {
        return when (rhs) {
            is VlangAnyTypeEx          -> true
            is VlangUnknownTypeEx      -> true
            is VlangVoidPtrTypeEx      -> true
            is VlangBuiltinArrayTypeEx -> true
            is VlangArrayTypeEx        -> true
            else                          -> false
        }
    }

    override fun isEqual(rhs: VlangTypeEx<*>): Boolean = true

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }
    }
}
