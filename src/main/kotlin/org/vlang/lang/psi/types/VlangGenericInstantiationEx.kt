package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

class VlangGenericInstantiationEx(
    val inner: VlangTypeEx,
    val specialization: List<VlangTypeEx>,
    anchor: PsiElement,
) : VlangBaseTypeEx(anchor) {

    override fun toString() = "$inner<${specialization.joinToString(", ")}>"

    override fun qualifiedName(): String = buildString {
        append(inner.qualifiedName())
        append("<")
        append(specialization.joinToString(", ") { it.qualifiedName() })
        append(">")
    }

    override fun readableName(context: PsiElement) = buildString {
        append(inner.readableName(context))
        append("<")
        append(specialization.joinToString(", ") { it.readableName(context) })
        append(">")
    }

    override fun isAssignableFrom(rhs: VlangTypeEx, project: Project): Boolean {
        return when (rhs) {
            is VlangAnyTypeEx              -> true
            is VlangUnknownTypeEx          -> true
            is VlangVoidPtrTypeEx          -> true
            is VlangGenericInstantiationEx -> true
            else                           -> false
        }
    }

    override fun isEqual(rhs: VlangTypeEx): Boolean {
        return rhs is VlangGenericInstantiationEx && rhs.qualifiedName() == qualifiedName()
    }

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }

        inner.accept(visitor)

        for (type in specialization) {
            type.accept(visitor)
        }
    }

    override fun substituteGenerics(nameMap: Map<String, VlangTypeEx>): VlangTypeEx {
        return VlangGenericInstantiationEx(
            inner.substituteGenerics(nameMap),
            specialization.map { it.substituteGenerics(nameMap) },
            anchor!!,
        )
    }
}
