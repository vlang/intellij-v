package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.VlangStructDeclaration
import org.vlang.lang.stubs.index.VlangClassLikeIndex

open class VlangStructTypeEx(private val name: String, anchor: PsiElement?) :
    VlangBaseTypeEx(anchor), VlangImportableTypeEx, VlangResolvableTypeEx<VlangStructDeclaration> {

    override fun toString() = name

    override fun qualifiedName(): String {
        if (moduleName.isEmpty()) {
            return "main.$name"
        }
        return "$moduleName.$name"
    }

    override fun readableName(context: PsiElement) = VlangCodeInsightUtil.getQualifiedName(context, anchor!!, name)

    override fun isAssignableFrom(rhs: VlangTypeEx, project: Project): Boolean {
        return when (rhs) {
            is VlangAnyTypeEx       -> true
            is VlangUnknownTypeEx   -> true
            is VlangVoidPtrTypeEx   -> true
            is VlangOptionTypeEx    -> if (rhs.inner == null) true else isAssignableFrom(rhs.inner, project)
            is VlangResultTypeEx    -> if (rhs.inner == null) true else isAssignableFrom(rhs.inner, project)
            is VlangPointerTypeEx   -> isAssignableFrom(rhs.inner, project)
            is VlangInterfaceTypeEx -> {
                // TODO: Check for interface implementation
                true
            }

            is VlangStructTypeEx    -> {
                val otherFqn = rhs.name

                // Temp approach
                return name == otherFqn
            }

            else                    -> false
        }
    }

    override fun isEqual(rhs: VlangTypeEx): Boolean {
        return rhs is VlangStructTypeEx && name == rhs.name
    }

    override fun accept(visitor: VlangTypeVisitor) {
        visitor.enter(this)
    }

    override fun substituteGenerics(nameMap: Map<String, VlangTypeEx>): VlangTypeEx = this

    override fun resolve(project: Project): VlangStructDeclaration? {
        val variants = VlangClassLikeIndex.find(qualifiedName(), project, null, null)
        if (variants.size == 1) {
            return variants.first() as? VlangStructDeclaration
        }

        return variants.firstOrNull { !(it.containingFile as VlangFile).isTestFile() } as? VlangStructDeclaration
    }
}
