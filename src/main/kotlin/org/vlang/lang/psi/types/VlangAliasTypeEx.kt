package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.lang.psi.VlangTypeAliasDeclaration
import org.vlang.lang.stubs.index.VlangNamesIndex

open class VlangAliasTypeEx(val name: String, val inner: VlangTypeEx, anchor: PsiElement) :
    VlangBaseTypeEx(anchor), VlangImportableTypeEx, VlangResolvableTypeEx<VlangTypeAliasDeclaration> {

    override fun toString() = name

    override fun qualifiedName(): String {
        if (moduleName.isEmpty()) {
            return "unnamed.$name"
        }
        return "$moduleName.$name"
    }

    override fun readableName(context: PsiElement) = VlangCodeInsightUtil.getQualifiedName(context, anchor!!, qualifiedName())

    override fun isAssignableFrom(rhs: VlangTypeEx, project: Project): Boolean {
        if (rhs.isAny) return true

        val unwrapped = this.unwrapAlias()
        val rhsUnwrapped = rhs.unwrapAlias()

        return unwrapped.isEqual(rhsUnwrapped) || unwrapped.isAssignableFrom(rhsUnwrapped, project)
    }

    override fun isEqual(rhs: VlangTypeEx): Boolean {
        return rhs is VlangAliasTypeEx && name == rhs.name
    }

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }

        inner.accept(visitor)
    }

    override fun substituteGenerics(nameMap: Map<String, VlangTypeEx>): VlangTypeEx {
        return VlangAliasTypeEx(name, inner.substituteGenerics(nameMap), anchor!!)
    }

    override fun resolve(project: Project): VlangTypeAliasDeclaration? {
        // TODO: own index?
        val variants = VlangNamesIndex.find(qualifiedName(), project, null)
        if (variants.isEmpty()) {
            return null
        }
        return variants.first { it is VlangTypeAliasDeclaration } as? VlangTypeAliasDeclaration
    }

    companion object {
        fun anyType(anchor: PsiElement) = object : VlangAliasTypeEx("Any", VlangAnyTypeEx.INSTANCE, anchor) {
            override val moduleName: String
                get() = VlangCodeInsightUtil.STUBS_MODULE
        }
    }
}
