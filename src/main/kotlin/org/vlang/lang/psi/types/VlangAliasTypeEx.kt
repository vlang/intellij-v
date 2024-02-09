package org.vlang.lang.psi.types

import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.lang.psi.VlangTypeAliasDeclaration
import org.vlang.lang.stubs.index.VlangNamesIndex

open class VlangAliasTypeEx(val name: String, val inner: VlangTypeEx, anchor: PsiElement) :
    VlangResolvableTypeEx<VlangTypeAliasDeclaration>(anchor), VlangImportableTypeEx {

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

    override fun resolveImpl(project: Project): VlangTypeAliasDeclaration? {
        // When file doesn't have a module name, we search all symbols only inside this file.
        val localFileResolve = containingFile != null && containingFile.getModule() == null
        val scope = if (localFileResolve) GlobalSearchScope.fileScope(containingFile!!) else null

        // TODO: own index?
        val variants = VlangNamesIndex.find(qualifiedName(), project, scope).filterIsInstance<VlangTypeAliasDeclaration>()
        if (variants.size == 1) {
            return variants.first()
        }

        return prioritize(containingFile, variants) as? VlangTypeAliasDeclaration
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VlangAliasTypeEx

        return name == other.name
    }

    override fun hashCode(): Int = name.hashCode()

    companion object {
        private val LOG = logger<VlangAliasTypeEx>()

        fun anyType(anchor: PsiElement) = object : VlangAliasTypeEx("Any", VlangAnyTypeEx.INSTANCE, anchor) {
            override val moduleName: String
                get() = VlangCodeInsightUtil.STUBS_MODULE
        }
    }
}
