package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.vlang.lang.psi.VlangResultType

class VlangResultTypeEx(raw: VlangResultType) : VlangBaseTypeEx<VlangResultType>(raw) {
    val inner = raw.type?.toEx()

    override fun toString() = "!".safeAppend(inner)

    override fun qualifiedName() = "!".safeAppend(inner?.qualifiedName())

    override fun readableName(context: PsiElement) = "!".safeAppend(inner?.readableName(context))

    override fun isAssignableFrom(rhs: VlangTypeEx<*>, project: Project): Boolean {
        return when (rhs) {
            is VlangAnyTypeEx     -> true
            is VlangUnknownTypeEx -> true
            is VlangVoidPtrTypeEx -> true
            is VlangResultTypeEx  -> if (rhs.inner == null) true else inner?.isAssignableFrom(rhs.inner, project) ?: false
            else                  -> false
        }
    }

    override fun isEqual(rhs: VlangTypeEx<*>): Boolean {
        if (rhs !is VlangResultTypeEx) {
            return false
        }

        if (rhs.inner == null && inner == null) {
            return true
        }

        return inner!!.isEqual(rhs.inner!!)
    }

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }

        if (inner != null) {
            visitor.enter(inner)
        }
    }
}