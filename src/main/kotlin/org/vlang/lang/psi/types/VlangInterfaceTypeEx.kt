package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.lang.psi.VlangInterfaceDeclaration
import org.vlang.lang.stubs.index.VlangClassLikeIndex

class VlangInterfaceTypeEx(val name: String, anchor: PsiElement) :
    VlangResolvableTypeEx<VlangInterfaceDeclaration>(anchor), VlangImportableTypeEx {

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

    override fun resolveImpl(project: Project): VlangInterfaceDeclaration? {
        // When file doesn't have a module name, we search all symbols only inside this file.
        val localFileResolve = containingFile != null && containingFile.getModule() == null
        val scope = if (localFileResolve) GlobalSearchScope.fileScope(containingFile!!) else null

        val variants = VlangClassLikeIndex.find(qualifiedName(), project, scope, null)
        if (variants.size == 1) {
            return variants.first() as? VlangInterfaceDeclaration
        }

        return prioritize(containingFile, variants) as? VlangInterfaceDeclaration
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VlangInterfaceTypeEx

        return name == other.name
    }

    override fun hashCode(): Int = name.hashCode()

    companion object {
        fun iError(anchor: PsiElement): VlangInterfaceTypeEx {
            return VlangInterfaceTypeEx("IError", anchor)
        }
    }
}
