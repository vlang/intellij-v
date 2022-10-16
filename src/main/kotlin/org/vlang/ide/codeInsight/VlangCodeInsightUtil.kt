package org.vlang.ide.codeInsight

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.*
import org.vlang.lang.psi.*
import org.vlang.lang.psi.types.VlangPrimitiveTypes

object VlangCodeInsightUtil {
    const val BUILTIN_MODULE = "builtin"

    fun ownerPresentableName(element: VlangNamedElement): String? {
        val owner = element.getOwner()
        if (owner is VlangFile) {
            return "module " +  owner.getModuleQualifiedName()
        } else if (owner !is VlangNamedElement) {
            return null
        }

        return "'${owner.name}'"
    }

    fun insideOrGuard(element: PsiElement): Boolean {
        return element.parentOfType<VlangOrBlockExpr>() != null
    }

    fun insideElseBlockIfGuard(element: PsiElement): Boolean {
        element.parentOfType<VlangElseStatement>(true) ?: return false
        val parentIf = element.parentOfType<VlangIfExpression>() ?: return false

        // if err used in nested if
        if (PsiTreeUtil.isAncestor(parentIf.expression, element, false)) {
            val secondParentIf = parentIf.parentOfType<VlangIfExpression>() ?: return false
            return secondParentIf.isGuard
        }

        return parentIf.isGuard
    }

    fun insideBuiltinModule(element: VlangCompositeElement): Boolean {
        val file = element.containingFile as VlangFile
        return file.getModuleQualifiedName() == BUILTIN_MODULE
    }

    fun insideTranslatedFile(element: VlangCompositeElement): Boolean {
        val file = element.containingFile as VlangFile
        return file.isTranslatedFile()
    }

    fun isExitCall(call: VlangCallExpr) = isBuiltinCall(call, "builtin.exit")

    fun isPanicCall(call: VlangCallExpr) = isBuiltinCall(call, "builtin.panic")

    fun findDuplicateImports(imports: List<VlangImportSpec>): MutableSet<VlangImportSpec> {
        val importsToDelete = mutableSetOf<VlangImportSpec>()
        val importsAsSet = imports.map { it.importedName to it }

        importsAsSet.forEach { (importName, spec) ->
            if (importsToDelete.contains(spec)) {
                return@forEach
            }

            val importsWithSameName = imports.filter { it.importedName == importName }
            if (importsWithSameName.size > 1) {
                importsWithSameName.subList(1, importsWithSameName.size).forEach { specToDelete ->
                    importsToDelete.add(specToDelete)
                }
            }
        }

        return importsToDelete
    }

    fun isImportUsed(import: VlangImportSpec): Boolean {
        return CachedValuesManager.getCachedValue(import) {
            CachedValueProvider.Result
                .create(
                    isImportUsedImpl(import),
                    PsiModificationTracker.MODIFICATION_COUNT
                )
        }
    }

    private fun isImportUsedImpl(import: VlangImportSpec): Boolean {
        if (import.selectiveImportList != null) {
            // TODO: support selective imports
            return true
        }

        val search = ReferencesSearch.search(import.importPath.lastPartPsi, GlobalSearchScope.allScope(import.project))
        if (search.findFirst() != null) {
            return true
        }

        if (import.importAlias != null) {
            val searchAlias = ReferencesSearch.search(import.importAlias!!, import.useScope)
            if (searchAlias.findFirst() != null) {
                return true
            }
        }

        return false
    }

    private fun isBuiltinCall(call: VlangCallExpr, name: String): Boolean {
        val ref = call.reference?.resolve() ?: return false
        return ref is VlangFunctionDeclaration && ref.getQualifiedName() == name
    }

    fun nonVlangName(name: String): Boolean {
        return name.startsWith("JS.") || name.startsWith("C.")
    }

    fun getQualifiedName(context: VlangCompositeElement, element: VlangNamedElement): String {
        val name = element.getQualifiedName() ?: return element.name ?: ""

        val contextFile = context.containingFile as VlangFile
        val contextModule = contextFile.getModuleQualifiedName()

        val elementFile = element.containingFile as VlangFile
        val elementModule = elementFile.getModuleQualifiedName()

        if (contextModule == elementModule) {
            return element.name ?: ""
        }

        if (name.startsWith("$contextModule.")) {
            return name.removePrefix("$contextModule.")
        }

        if (name.count { it == '.' } == 1) {
            if (name.startsWith("$BUILTIN_MODULE.")) {
                return name.removePrefix("$BUILTIN_MODULE.")
            }
            return name
        }

        val parts = name.split('.')
        return parts[parts.size - 2] + "." + parts[parts.size - 1]
    }

    fun sameModule(firstFile: PsiFile, secondFile: PsiFile): Boolean {
        val containingDirectory = firstFile.containingDirectory
        if (containingDirectory == null || containingDirectory != secondFile.containingDirectory) {
            return false
        }
        if (firstFile is VlangFile && secondFile is VlangFile) {
            val referencePackage = firstFile.packageName
            val definitionPackage = secondFile.packageName
            return referencePackage != null && referencePackage == definitionPackage
        }
        return true
    }

    fun sameModule(first: VlangCompositeElement, second: VlangCompositeElement): Boolean {
        val firstFile = first.containingFile as VlangFile
        val firstModule = firstFile.getModuleQualifiedName()

        val secondFile = second.containingFile as VlangFile
        val secondModule = secondFile.getModuleQualifiedName()

        return firstModule == secondModule
    }

    fun isTypeCast(call: VlangCallExpr): Boolean {
        val expr = call.expression as? VlangReferenceExpression ?: return false
        val name = expr.getIdentifier().text ?: return false

        return VlangPrimitiveTypes.values().find { it.value == name } != null
    }
}
