package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.util.childrenOfType
import org.vlang.lang.psi.VlangGenericParametersOwner
import org.vlang.lang.psi.VlangNamedElement
import org.vlang.lang.psi.VlangTypeOwner

class VlangGenericInstantiationEx(
    val inner: VlangTypeEx,
    val specialization: List<VlangTypeEx>,
    anchor: PsiElement,
) : VlangBaseTypeEx(anchor), VlangResolvableTypeEx<VlangNamedElement> {

    override fun toString() = "$inner[${specialization.joinToString(", ")}]"

    override fun qualifiedName(): String = buildString {
        append(inner.qualifiedName())
        append("[")
        append(specialization.joinToString(", ") { it.qualifiedName() })
        append("]")
    }

    override fun readableName(context: PsiElement) = buildString {
        append(inner.readableName(context))
        append("[")
        append(specialization.joinToString(", ") { it.readableName(context) })
        append("]")
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

    override fun resolve(project: Project): VlangNamedElement? {
        if (inner is VlangResolvableTypeEx<*>) {
            return inner.resolve(project)
        }

        return null
    }

    override fun substituteGenerics(nameMap: Map<String, VlangTypeEx>): VlangTypeEx {
        return VlangGenericInstantiationEx(
            inner.substituteGenerics(nameMap),
            specialization.map { it.substituteGenerics(nameMap) },
            anchor!!,
        )
    }

    fun specializationMap(project: Project): Map<String, VlangTypeEx> {
        val genericTs = extractInstantiationTs(project)
        if (genericTs.isEmpty()) {
            return emptyMap()
        }

        return genericTs.zip(specialization).toMap()
    }

    fun extractInstantiationTs(project: Project): List<String> {
        if (inner !is VlangResolvableTypeEx<*>) {
            return emptyList()
        }

        val innerResolved = inner.resolve(project) as? VlangTypeOwner ?: return emptyList()
        val resolvedType = innerResolved.childrenOfType<VlangGenericParametersOwner>().firstOrNull() ?: return emptyList()
        return extractGenericParameters(resolvedType)
    }

    private fun extractGenericParameters(resolvedType: VlangGenericParametersOwner) =
        resolvedType.genericParameters?.parameters?.map { it.name!! } ?: emptyList()
}
