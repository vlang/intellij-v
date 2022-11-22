package org.vlang.ide.codeInsight

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.*
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.*
import org.vlang.lang.psi.types.*
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.utils.parentNth
import org.vlang.utils.parentOfType

object VlangCodeInsightUtil {
    const val BUILTIN_MODULE = "builtin"
    private const val ERR_VARIABLE = "err"

    fun isErrVariable(element: PsiElement): Boolean {
        return element.elementType == VlangTypes.IDENTIFIER && element.text == ERR_VARIABLE
    }

    fun ownerPresentableName(element: VlangNamedElement): String? {
        val owner = element.getOwner()
        if (owner is VlangFile) {
            return "module " + owner.getModuleQualifiedName()
        } else if (owner !is VlangNamedElement) {
            return null
        }

        return "'${owner.name}'"
    }

    fun getCallExpr(element: PsiElement): VlangCallExpr? {
        val parentValue = element.parentOfType<VlangValue>(VlangBlock::class)
        if (parentValue != null) {
            return parentValue.parentNth(3)
        }
        return element.parentOfType(VlangBlock::class)
    }

    fun getCalledParams(callExpr: VlangCallExpr?): List<VlangTypeEx>? {
        val resolved = callExpr?.resolve() as? VlangSignatureOwner ?: return null
        val params = resolved.getSignature()?.parameters?.paramDefinitionList
        return params?.map { it.type.toEx() }
    }

    fun isAllowedParamsForTrailingStruct(params: List<VlangParamDefinition>, paramTypes: List<VlangTypeEx>): Boolean {
        if (paramTypes.none { it is VlangStructTypeEx }) return false
        if (params.isEmpty()) return false

        val structType = paramTypes.last()
        if (params.size > 1 && structType !is VlangStructTypeEx) return false
        return true
    }

    fun isArrayMethodCall(callExpr: VlangCallExpr): Boolean {
        val function = callExpr.resolve() ?: return false
        return function is VlangMethodDeclaration && function.receiverType?.textMatches("array") == true
    }

    fun insideOrGuard(element: PsiElement): Boolean {
        return element.parentOfType<VlangOrBlockExpr>() != null
    }

    fun takeZeroArguments(owner: VlangSignatureOwner): Boolean {
        return owner.getSignature()?.parameters?.paramDefinitionList?.isEmpty() ?: false
    }

    fun insideElseBlockIfGuard(element: PsiElement): Boolean {
        element.parentOfType<VlangElseStatement>(true) ?: return false
        val parentIf = element.parentOfType<VlangIfExpression>() ?: return false

        // if err used in nested if
        if (PsiTreeUtil.isAncestor(parentIf.expression, element, false)) {
            val secondParentIf = parentIf.parentOfType<VlangIfExpression>(false) ?: return false
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

    fun getQualifiedName(context: PsiElement, anchor: PsiElement, name: String): String {
        val contextFile = context.containingFile as VlangFile
        val contextModule = contextFile.getModuleQualifiedName()

        val elementFile = anchor.containingFile as VlangFile
        val elementModule = elementFile.getModuleQualifiedName()

        if (contextModule == elementModule) {
            return name
        }

        if (name.startsWith("$contextModule.")) {
            return name.removePrefix("$contextModule.")
        }

        if (name.startsWith("$BUILTIN_MODULE.")) {
            return name.removePrefix("$BUILTIN_MODULE.")
        }

        return name
    }

    fun sameModule(firstFile: PsiFile, secondFile: PsiFile): Boolean {
        val containingDirectory = firstFile.containingDirectory
        if (containingDirectory == null || containingDirectory != secondFile.containingDirectory) {
            return false
        }
        if (firstFile is VlangFile && secondFile is VlangFile) {
            val referenceModule = firstFile.getModuleQualifiedName()
            val definitionModule = secondFile.getModuleQualifiedName()
            return referenceModule == definitionModule
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

    fun getReturnType(resolved: PsiElement): VlangTypeEx? {
        if (resolved is VlangFunctionOrMethodDeclaration) {
            return resolved.getSignature()?.result?.type?.toEx() ?: VlangVoidTypeEx.INSTANCE
        }

        if (resolved is VlangTypeOwner) {
            val type = resolved.getType(null)
            if (type is VlangFunctionTypeEx) {
                return type.result
            }
        }

        return null
    }
}
