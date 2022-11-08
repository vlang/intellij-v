package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.vlang.lang.psi.VlangArrayType

class VlangArrayTypeEx(raw: VlangArrayType) : VlangBaseTypeEx<VlangArrayType>(raw) {
    val inner = raw.type.toEx()

    override fun toString() = "[]".safeAppend(inner)

    override fun qualifiedName(): String = "[]".safeAppend(inner.qualifiedName())

    override fun readableName(context: PsiElement) = "[]".safeAppend(inner.readableName(context))

    override fun isAssignableFrom(rhs: VlangTypeEx<*>, project: Project): Boolean {
        return when (rhs) {
            is VlangAnyTypeEx          -> true
            is VlangVoidPtrTypeEx      -> true
            is VlangUnknownTypeEx      -> true
            is VlangBuiltinArrayTypeEx -> true
            is VlangArrayTypeEx        -> inner.isAssignableFrom(rhs.inner, project)
            else                       -> false
        }
    }

    override fun isEqual(rhs: VlangTypeEx<*>): Boolean {
        return rhs is VlangArrayTypeEx && inner.isEqual(rhs.inner)
    }

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }

        visitor.enter(inner)
    }
}
