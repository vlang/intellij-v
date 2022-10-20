package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.lang.psi.VlangInterfaceDeclaration
import org.vlang.lang.psi.VlangInterfaceType

class VlangInterfaceTypeEx(raw: VlangInterfaceType) : VlangBaseTypeEx<VlangInterfaceType>(raw), VlangImportableTypeEx {
    private val decl = raw.parent as VlangInterfaceDeclaration
    private val name = decl.getQualifiedName() ?: ANON

    override fun toString() = name

    override fun qualifiedName() = name

    override fun readableName(context: PsiElement) = VlangCodeInsightUtil.getQualifiedName(context, decl)

    override fun isAssignableFrom(rhs: VlangTypeEx<*>, project: Project): Boolean {
        return true // TODO: Implement this
    }

    override fun isEqual(rhs: VlangTypeEx<*>): Boolean {
        return rhs is VlangInterfaceTypeEx && name == rhs.name
    }

    override fun accept(visitor: VlangTypeVisitor) {
        visitor.enter(this)
    }
}
