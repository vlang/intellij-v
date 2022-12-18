package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.VlangNamedElement
import org.vlang.lang.psi.VlangStructDeclaration
import org.vlang.lang.stubs.index.VlangClassLikeIndex

open class VlangStructTypeEx(private val name: String, anchor: PsiElement?) :
    VlangBaseTypeEx(anchor), VlangImportableTypeEx, VlangResolvableTypeEx<VlangStructDeclaration> {

    override fun toString() = name

    override fun qualifiedName(): String {
        if (moduleName.isEmpty()) {
            return "unnamed.$name"
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VlangStructTypeEx

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int = name.hashCode()

    override fun resolve(project: Project): VlangStructDeclaration? {
        // When file doesn't have a module name, we search all symbols only inside this file.
        val localFileResolve = containingFile != null && containingFile.getModule() == null
        val scope = if (localFileResolve) GlobalSearchScope.fileScope(containingFile!!) else null

        val variants = VlangClassLikeIndex.find(qualifiedName(), project, scope, null)
        if (variants.size == 1) {
            return variants.first() as? VlangStructDeclaration
        }

        val containsVariantFromModules = variants.any { fromModules(it) }

        if (containsVariantFromModules) {
            val variantsWithoutModules = variants.filter { fromModules(it) }
            if (variantsWithoutModules.isEmpty()) {
                return preferNonTestsFirst(variants) as? VlangStructDeclaration
            }
            return preferNonTestsFirst(variantsWithoutModules) as? VlangStructDeclaration
        }

        return preferNonTestsFirst(variants) as? VlangStructDeclaration
    }

    private fun preferNonTestsFirst(variants: Collection<VlangNamedElement>): VlangNamedElement? {
        if (variants.size == 1) {
            return variants.first()
        }

        return variants
            .firstOrNull {
                !(it.containingFile as VlangFile).isTestFile()
            }
    }

    private fun fromModules(it: VlangNamedElement) = it.containingFile.virtualFile.path.contains(".vmodules")

    companion object {
        val UnknownCDeclarationStruct = object : VlangStructTypeEx("UnknownCDeclaration", null) {
            override val moduleName: String
                get() = VlangCodeInsightUtil.STUBS_MODULE
        }
    }
}
