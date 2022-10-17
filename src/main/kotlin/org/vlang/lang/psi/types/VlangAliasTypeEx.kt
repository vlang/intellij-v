package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.lang.psi.VlangAliasType
import org.vlang.lang.psi.VlangCompositeElement
import org.vlang.lang.psi.VlangTypeAliasDeclaration

class VlangAliasTypeEx(raw: VlangAliasType) : VlangBaseTypeEx<VlangAliasType>(raw), VlangImportableTypeEx {
    private val decl = raw.parent as VlangTypeAliasDeclaration
    private val name = decl.getQualifiedName() ?: ANON
    private val left = VlangSimpleTypeEx(raw.type)
    private val right = raw.typeUnionList?.typeList?.firstOrNull().toEx()

    override fun toString() = buildString {
        append(name)
        append(" = ")
        append(right)
    }

    override fun qualifiedName() = name

    override fun readableName(context: VlangCompositeElement) = VlangCodeInsightUtil.getQualifiedName(context, decl)

    override fun isAssignableFrom(rhs: VlangTypeEx<*>, project: Project): Boolean {
        return true // TODO: implement this
    }

    override fun isEqual(rhs: VlangTypeEx<*>): Boolean {
        return rhs is VlangAliasTypeEx && name == rhs.name
    }

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }

        if (!visitor.enter(left)) {
            return
        }

        if (!visitor.enter(right)) {
            return
        }
    }
}
