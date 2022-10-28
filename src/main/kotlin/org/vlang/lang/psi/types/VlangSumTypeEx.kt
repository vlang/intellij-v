package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.lang.psi.VlangAliasType
import org.vlang.lang.psi.VlangTypeAliasDeclaration

class VlangSumTypeEx(raw: VlangAliasType) : VlangBaseTypeEx<VlangAliasType>(raw), VlangImportableTypeEx {
    private val decl = raw.parent as VlangTypeAliasDeclaration
    private val name = decl.getQualifiedName() ?: ANON
    private val rightList = emptyList<VlangTypeEx<*>>() // TODO

    override fun toString() = buildString {
        append(name)
        append(" = ")
        append(rightList.joinToString(" | ") { it.toString() })
    }

    override fun qualifiedName() = name

    override fun readableName(context: PsiElement) = VlangCodeInsightUtil.getQualifiedName(context, decl)

    override fun isAssignableFrom(rhs: VlangTypeEx<*>, project: Project): Boolean {
        return true // TODO: implement this
    }

    override fun isEqual(rhs: VlangTypeEx<*>): Boolean {
        return true // TODO: implement this
    }

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }

        for (right in rightList) {
            if (!visitor.enter(right)) {
                return
            }
        }
    }
}
