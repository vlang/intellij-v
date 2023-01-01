package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.lang.psi.VlangInterfaceDeclaration
import org.vlang.lang.stubs.index.VlangClassLikeIndex

class VlangInterfaceTypeEx(val name: String, anchor: PsiElement) :
    VlangBaseTypeEx(anchor), VlangImportableTypeEx, VlangResolvableTypeEx<VlangInterfaceDeclaration> {

    override fun toString() = name

    override fun qualifiedName(): String {
        if (name == "IError") {
            return "builtin.IError"
        }

        if (moduleName.isEmpty()) {
            return name
        }
        return "$moduleName.$name"
    }

    override fun readableName(context: PsiElement) = VlangCodeInsightUtil.getQualifiedName(context, anchor!!, qualifiedName())

    override fun isAssignableFrom(rhs: VlangTypeEx, project: Project): Boolean {
        if (rhs.isAny) return true
        if (rhs is VlangInterfaceTypeEx) {
            return this.qualifiedName() == rhs.qualifiedName()
        }
        return false
    }

    override fun isEqual(rhs: VlangTypeEx): Boolean {
        return rhs is VlangInterfaceTypeEx && qualifiedName() == rhs.qualifiedName()
    }

    override fun accept(visitor: VlangTypeVisitor) {
        visitor.enter(this)
    }

    override fun substituteGenerics(nameMap: Map<String, VlangTypeEx>): VlangTypeEx = this

    override fun resolve(project: Project): VlangInterfaceDeclaration? {
        val variants = VlangClassLikeIndex.find(qualifiedName(), project, null, null)
        return variants.firstOrNull() as? VlangInterfaceDeclaration
    }

    companion object {
        fun iError(anchor: PsiElement): VlangInterfaceTypeEx {
            return VlangInterfaceTypeEx("IError", anchor)
        }
    }
}
