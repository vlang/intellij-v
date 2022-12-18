package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.lang.psi.VlangTypeAliasDeclaration
import org.vlang.lang.stubs.index.VlangNamesIndex

open class VlangSumTypeEx(val name: String, val types: List<VlangTypeEx>, anchor: PsiElement?) :
    VlangBaseTypeEx(anchor), VlangImportableTypeEx, VlangResolvableTypeEx<VlangTypeAliasDeclaration> {

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

    override fun readableName(context: PsiElement) = VlangCodeInsightUtil.getQualifiedName(context, anchor!!, name)

    override fun isAssignableFrom(rhs: VlangTypeEx, project: Project): Boolean {
        return true // TODO: implement this
    }

    override fun isEqual(rhs: VlangTypeEx): Boolean {
        return true // TODO: implement this
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

    override fun resolve(project: Project): VlangTypeAliasDeclaration? {
        // TODO: own index?
        val variants = VlangNamesIndex.find(qualifiedName(), project, null)
        if (variants.isEmpty()) {
            return null
        }
        return variants.first { it is VlangTypeAliasDeclaration } as? VlangTypeAliasDeclaration
    }
}
