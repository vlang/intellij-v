package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.lang.psi.VlangFieldDefinition
import org.vlang.lang.psi.VlangTypeAliasDeclaration
import org.vlang.lang.stubs.index.VlangNamesIndex

open class VlangSumTypeEx(val name: String, val types: List<VlangTypeEx>, anchor: PsiElement?) :
    VlangResolvableTypeEx<VlangTypeAliasDeclaration>(anchor), VlangImportableTypeEx {

    override fun toString() = buildString {
        append(name)
        append(" = ")
        append(types.joinToString(" | ") { it.toString() })
    }

    override fun qualifiedName(): String {
        if (moduleName.isEmpty()) {
            return "unnamed.$name"
        }
        return "$moduleName.$name"
    }

    override fun readableName(context: PsiElement) = VlangCodeInsightUtil.getQualifiedName(context, anchor!!, qualifiedName())

    override fun isAssignableFrom(rhs: VlangTypeEx, project: Project): Boolean {
        if (rhs.isAny) return true
        if (rhs is VlangSumTypeEx) {
            return this.qualifiedName() == rhs.qualifiedName()
        }
        return this.types.any { it.isAssignableFrom(rhs, project) }
    }

    override fun isEqual(rhs: VlangTypeEx): Boolean {
        return rhs is VlangSumTypeEx && qualifiedName() == rhs.qualifiedName()
    }

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }

        for (right in types) {
            right.accept(visitor)
        }
    }

    override fun substituteGenerics(nameMap: Map<String, VlangTypeEx>): VlangTypeEx {
        return VlangSumTypeEx(name, types.map { it.substituteGenerics(nameMap) }, anchor!!)
    }

    override fun resolveImpl(project: Project): VlangTypeAliasDeclaration? {
        // TODO: own index?
        val variants = VlangNamesIndex.find(qualifiedName(), project, null)
        if (variants.isEmpty()) {
            return null
        }
        return variants.first { it is VlangTypeAliasDeclaration } as? VlangTypeAliasDeclaration
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VlangSumTypeEx

        return name == other.name
    }

    override fun hashCode(): Int = name.hashCode()

    fun getFields(project: Project): List<VlangFieldDefinition> {
        return getFieldsList(project).flatten()
    }

    fun findCommonFields(project: Project): List<VlangFieldDefinition> {
        val fields = getFieldsList(project)
        val commonFields = fields.first().filter { field ->
            fields.all { fieldList -> fieldList.any { it.name == field.name } }
        }
        return commonFields
    }

    private fun getFieldsList(project: Project) = types.mapNotNull {
        val singleType = it.unwrapPointer().unwrapAlias().unwrapPointer()
        if (singleType is VlangStructTypeEx) {
            singleType.fieldsList(project).toSet()
        } else {
            null
        }
    }
}
