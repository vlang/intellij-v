package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.vlang.lang.psi.VlangSignature
import org.vlang.lang.psi.VlangSignatureOwner

class VlangFunctionTypeEx(val params: List<VlangTypeEx>, val result: VlangTypeEx?, val signature: VlangSignature) : VlangBaseTypeEx(signature) {
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

    override fun qualifiedName(): String = buildString {
        append("fn ")
        append("(")
        append(params.joinToString(", ") { it.qualifiedName() })
        append(")")
        if (result != null) {
            append(" ")
            append(result.qualifiedName())
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

    override fun isAssignableFrom(rhs: VlangTypeEx, project: Project): Boolean {
        if (rhs.isAny) return true
        return isEqual(rhs)
    }

    override fun isEqual(rhs: VlangTypeEx): Boolean {
        if (rhs !is VlangFunctionTypeEx) return false

        if (params.size != rhs.params.size) {
            return false
        }

        if (result != null && rhs.result == null) {
            return false
        }

        if (result == null && rhs.result != null) {
            return false
        }

        if (result != null && rhs.result != null && !result.isEqual(rhs.result)) {
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
            param.accept(visitor)
        }

        result?.accept(visitor)
    }

    override fun substituteGenerics(nameMap: Map<String, VlangTypeEx>): VlangTypeEx {
        return VlangFunctionTypeEx(
            params.map { it.substituteGenerics(nameMap) },
            result?.substituteGenerics(nameMap),
            signature
        )
    }

    companion object {
        fun from(signatureOwner: VlangSignatureOwner): VlangFunctionTypeEx? {
            val signature = signatureOwner.getSignature() ?: return null
            val params = signature.parameters.paramDefinitionList.map { it.type.toEx() }
            val result = signature.result?.type?.toEx()
            return VlangFunctionTypeEx(params, result, signature)
        }
    }
}
