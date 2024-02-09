package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.lang.psi.VlangFieldDefinition
import org.vlang.lang.psi.VlangStructDeclaration
import org.vlang.lang.stubs.index.VlangClassLikeIndex

open class VlangStructTypeEx(private val name: String, anchor: PsiElement?) :
    VlangResolvableTypeEx<VlangStructDeclaration>(anchor), VlangImportableTypeEx {

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
        if (rhs is VlangStructTypeEx) {
            return this.qualifiedName() == rhs.qualifiedName()
        }
        return false
    }

    override fun isEqual(rhs: VlangTypeEx): Boolean {
        return rhs is VlangStructTypeEx && qualifiedName() == rhs.qualifiedName()
    }

    override fun accept(visitor: VlangTypeVisitor) {
        visitor.enter(this)
    }

    override fun substituteGenerics(nameMap: Map<String, VlangTypeEx>): VlangTypeEx = this

    override fun resolveImpl(project: Project): VlangStructDeclaration? {
        // When file doesn't have a module name, we search all symbols only inside this file.
        val localFileResolve = containingFile != null && containingFile.getModule() == null
        val scope = if (localFileResolve) GlobalSearchScope.fileScope(containingFile!!) else null

        val variants = VlangClassLikeIndex.find(qualifiedName(), project, scope, null)
        if (variants.size == 1) {
            return variants.first() as? VlangStructDeclaration
        }

        return prioritize(containingFile, variants) as? VlangStructDeclaration
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VlangStructTypeEx

        return name == other.name
    }

    override fun hashCode(): Int = name.hashCode()

    fun fieldsList(project: Project): List<VlangFieldDefinition> {
        val structDecl = resolve(project) ?: return emptyList()
        val type = structDecl.structType
        return type.fieldList
    }

    companion object {
        val UnknownCDeclarationStruct = object : VlangStructTypeEx("UnknownCDeclaration", null) {
            override val moduleName: String
                get() = VlangCodeInsightUtil.STUBS_MODULE
        }

        val ArrayInit = object : VlangStructTypeEx("ArrayInit", null) {
            override val moduleName: String
                get() = VlangCodeInsightUtil.STUBS_MODULE
        }

        val ChanInit = object : VlangStructTypeEx("ChanInit", null) {
            override val moduleName: String
                get() = VlangCodeInsightUtil.STUBS_MODULE
        }
    }
}
