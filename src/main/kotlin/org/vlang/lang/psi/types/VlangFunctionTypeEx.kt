package org.vlang.lang.psi.types

import org.vlang.lang.psi.VlangCompositeElement
import org.vlang.lang.psi.VlangFunctionType

class VlangFunctionTypeEx(raw: VlangFunctionType) : VlangBaseTypeEx<VlangFunctionType>(raw) {
    private val signature = raw.signature
    private val params = signature?.parameters?.parametersListWithTypes?.map { (_, type) -> type.toEx() } ?: emptyList()
    private val result = signature?.result?.type?.toEx()

    override fun toString() = buildString {
        append("fn ")
        append("(")
        append(params.joinToString(", ") { it.toString() })
        append(")")
        append(" ")
        append(result)
    }

    override fun readableName(context: VlangCompositeElement) = buildString {
        append("fn ")
        append("(")
        append(params.joinToString(", ") { it.readableName(context) })
        append(")")
        append(" ")
        append(result?.readableName(context))
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
