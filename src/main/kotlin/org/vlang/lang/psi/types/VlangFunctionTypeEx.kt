package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.vlang.lang.psi.VlangFunctionType
import org.vlang.lang.psi.VlangSignature

class VlangFunctionTypeEx(raw: VlangFunctionType?, signature: VlangSignature? = null) : VlangBaseTypeEx<VlangFunctionType?>(raw) {
    val signature = signature ?: raw?.getSignature()
    val params = this.signature?.parameters?.parametersListWithTypes?.map { (_, type) -> type.toEx() } ?: emptyList()
    val result = this.signature?.result?.type?.toEx()

    override fun toString() = buildString {
        append("fn ")
        append("(")
        append(params.joinToString(", ") { it.toString() })
        append(")")
        if (result != null) {
            append(" ")
            append(result)
        }
    }

    override fun readableName(context: PsiElement) = buildString {
        append("fn ")
        append("(")
        append(params.joinToString(", ") { it.readableName(context) })
        append(")")
        if (result != null) {
            append(" ")
            append(result.readableName(context))
        }
    }

    override fun isAssignableFrom(rhs: VlangTypeEx<*>, project: Project): Boolean {
        return true // TODO: implement this
    }

    override fun isEqual(rhs: VlangTypeEx<*>): Boolean {
        if (rhs !is VlangFunctionTypeEx) return false

        if (params.size != rhs.params.size) {
            return false
        }

        if (result == null || rhs.result == null || !result.isEqual(rhs.result)) {
            return false
        }

        return params.zip(rhs.params)
            .all { (left, right) ->
                left.isEqual(right)
            }
    }

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }

        for (param in params) {
            if (!visitor.enter(param)) {
                return
            }
        }

        if (result != null) {
            if (!visitor.enter(result)) {
                return
            }
        }
    }
}
