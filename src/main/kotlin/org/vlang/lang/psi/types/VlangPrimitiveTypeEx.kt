package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.vlang.lang.psi.VlangType

class VlangPrimitiveTypeEx(raw: VlangType, private val name: VlangPrimitiveTypes) : VlangBaseTypeEx<VlangType>(raw) {
    override fun toString(): String = name.value

    override fun readableName(context: PsiElement): String = name.value

    fun isNumeric(): Boolean {
        return name.numeric
    }

    override fun isAssignableFrom(rhs: VlangTypeEx<*>, project: Project): Boolean {
        return when (rhs) {
            is VlangAnyTypeEx       -> true
            is VlangUnknownTypeEx   -> true
            is VlangVoidPtrTypeEx   -> true
            is VlangPrimitiveTypeEx -> {
                if (name == rhs.name) {
                    return true
                }

                val lhsType = name
                val rhsType = rhs.name

                val lhsName = lhsType.value
                val rhsName = rhsType.value

                if (lhsName.startsWith("i") && rhsName.startsWith("i")) {
                    return rhs.name.size <= name.size
                }

                if (lhsName.startsWith("u") && rhsName.startsWith("u")) {
                    return rhs.name.size <= name.size
                }

                if (lhsName.startsWith("f") && rhsName.startsWith("f")) {
                    return rhs.name.size <= name.size
                }

                false
            }

            else                    -> false
        }
    }

    override fun isEqual(rhs: VlangTypeEx<*>): Boolean {
        return rhs is VlangPrimitiveTypeEx && name == rhs.name
    }

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }
    }
}
