package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.lang.psi.VlangCompositeElement
import org.vlang.lang.psi.VlangEnumDeclaration
import org.vlang.lang.psi.VlangEnumType

class VlangEnumTypeEx(raw: VlangEnumType) : VlangBaseTypeEx<VlangEnumType>(raw), VlangImportableTypeEx {
    private val decl = raw.parent as VlangEnumDeclaration
    private val name = decl.getQualifiedName() ?: ANON

    override fun toString() = name

    override fun qualifiedName() = name

    override fun readableName(context: VlangCompositeElement) = VlangCodeInsightUtil.getQualifiedName(context, decl)

    override fun isAssignableFrom(rhs: VlangTypeEx<*>, project: Project): Boolean {
        return when (rhs) {
            is VlangAnyTypeEx     -> true
            is VlangUnknownTypeEx -> true
            is VlangVoidPtrTypeEx -> true
            is VlangEnumTypeEx    -> {
                val otherFqn = rhs.name

                // Temp approach?
                return name == otherFqn
            }

            else                  -> false
        }
    }

    override fun isEqual(rhs: VlangTypeEx<*>): Boolean {
        return rhs is VlangEnumTypeEx && name == rhs.name
    }

    override fun accept(visitor: VlangTypeVisitor) {
        visitor.enter(this)
    }
}
