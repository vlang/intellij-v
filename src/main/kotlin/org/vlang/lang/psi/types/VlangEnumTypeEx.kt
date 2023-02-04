package org.vlang.lang.psi.types

import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.lang.psi.VlangEnumDeclaration
import org.vlang.lang.stubs.index.VlangClassLikeIndex

open class VlangEnumTypeEx(val name: String, anchor: PsiElement?) :
    VlangBaseTypeEx(anchor), VlangImportableTypeEx, VlangResolvableTypeEx<VlangEnumDeclaration> {

    override fun toString() = name

    override fun qualifiedName(): String {
        if (moduleName.isEmpty()) {
            return name
        }
        return "$moduleName.$name"
    }

    override fun readableName(context: PsiElement) = VlangCodeInsightUtil.getQualifiedName(context, anchor!!, qualifiedName())

    override fun isAssignableFrom(rhs: VlangTypeEx, project: Project): Boolean {
        if (rhs.isAny) return true
        if (rhs is VlangEnumTypeEx) {
            return this.qualifiedName() == rhs.qualifiedName()
        }
        return false
    }

    override fun isEqual(rhs: VlangTypeEx): Boolean {
        return rhs is VlangEnumTypeEx && qualifiedName() == rhs.qualifiedName()
    }

    override fun accept(visitor: VlangTypeVisitor) {
        visitor.enter(this)
    }

    override fun substituteGenerics(nameMap: Map<String, VlangTypeEx>): VlangTypeEx = this

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        other as VlangEnumTypeEx

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int = name.hashCode()

    override fun resolve(project: Project): VlangEnumDeclaration? {
        if (anchor == null) {
            LOG.info("Skip caching for ${qualifiedName()} because anchor is null")
            return resolveImpl(project)
        }

        return CachedValuesManager.getCachedValue(anchor) {
            val result = resolveImpl(project)
            CachedValueProvider.Result(result, containingFile)
        }
    }

    private fun resolveImpl(project: Project): VlangEnumDeclaration? {
        // When file doesn't have a module name, we search all symbols only inside this file.
        val localFileResolve = containingFile != null && containingFile.getModule() == null
        val scope = if (localFileResolve) GlobalSearchScope.fileScope(containingFile!!) else null

        val variants = VlangClassLikeIndex.find(qualifiedName(), project, scope, null)
        if (variants.size == 1) {
            return variants.first() as? VlangEnumDeclaration
        }

        return prioritize(containingFile, variants) as? VlangEnumDeclaration
    }

    companion object {
        private val LOG = logger<VlangEnumTypeEx>()

        val FLAG_ENUM = object : VlangEnumTypeEx("FlagEnum", null) {
            override val moduleName: String
                get() = VlangCodeInsightUtil.STUBS_MODULE
        }
    }
}
