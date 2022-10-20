package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.lang.psi.VlangStructDeclaration
import org.vlang.lang.psi.VlangStructType

class VlangStructTypeEx(raw: VlangStructType) : VlangBaseTypeEx<VlangStructType>(raw), VlangImportableTypeEx {
    private val decl = raw.parent as VlangStructDeclaration
    private val name = decl.getQualifiedName() ?: ANON

    override fun toString() = name

    override fun qualifiedName() = name

    override fun readableName(context: PsiElement) = VlangCodeInsightUtil.getQualifiedName(context, decl)

    override fun isAssignableFrom(rhs: VlangTypeEx<*>, project: Project): Boolean {
        return when (rhs) {
            is VlangAnyTypeEx         -> true
            is VlangUnknownTypeEx     -> true
            is VlangVoidPtrTypeEx     -> true
            is VlangNullableTypeEx    -> if (rhs.inner == null) true else isAssignableFrom(rhs.inner, project)
            is VlangNotNullableTypeEx -> if (rhs.inner == null) true else isAssignableFrom(rhs.inner, project)
            is VlangPointerTypeEx     -> if (rhs.inner == null) true else isAssignableFrom(rhs.inner, project)
            is VlangInterfaceTypeEx   -> {
                // TODO: Check for interface implementation
                true
            }

            is VlangStructTypeEx      -> {
                val otherFqn = rhs.name

                // Temp approach
                return name == otherFqn
            }

            else                      -> false
        }
    }

    override fun isEqual(rhs: VlangTypeEx<*>): Boolean {
        return rhs is VlangStructTypeEx && name == rhs.name
    }

    override fun accept(visitor: VlangTypeVisitor) {
        visitor.enter(this)
    }
}
