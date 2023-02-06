package org.vlang.lang.psi.impl

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import org.vlang.configurations.VlangConfiguration
import org.vlang.lang.psi.*
import org.vlang.lang.psi.types.*
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.lang.stubs.index.VlangMethodIndex

object VlangLangUtil {
    fun findMethod(project: Project, type: VlangTypeEx, name: String): VlangMethodDeclaration? {
        return getMethodList(project, type).firstOrNull { it.name == name }
    }

    fun getErrVariableDefinition(project: Project): VlangConstDefinition? {
        val stubDir = VlangConfiguration.getInstance(project).stubsLocation ?: return null
        val psiManager = PsiManager.getInstance(project)
        val stubFile = getStubFile("errors.v", stubDir, psiManager) ?: return null
        return stubFile.getConstants().firstOrNull { it.name == "err" }
    }

    private fun getStubFile(name: String, stubDir: VirtualFile, psiManager: PsiManager): VlangFile? {
        val stubFile = stubDir.findChild(name) ?: return null
        return psiManager.findFile(stubFile) as? VlangFile ?: return null
    }

    fun getUsedReceiverNameOrDefault(
        methods: List<VlangMethodDeclaration>,
        default: String?,
    ) = methods.firstOrNull()?.receiver?.name ?: default

    fun importTypesFromSignature(signature: VlangSignature, file: VlangFile) {
        val currentModule = file.getModuleQualifiedName()
        val typesToImport = findTypesForImport(signature, currentModule)
        if (typesToImport.isEmpty()) {
            return
        }

        typesToImport.forEach { file.addImport(it.module(), null) }
    }

    private fun findTypesForImport(signature: VlangSignature, currentModule: String): MutableSet<VlangTypeEx> {
        val typesToImport = mutableSetOf<VlangTypeEx>()

        val types = signature.parameters.paramDefinitionList.mapNotNull { it.type }
        types.forEach { type ->
            type.toEx().accept(object : VlangTypeVisitor {
                override fun enter(type: VlangTypeEx): Boolean {
                    if (type is VlangImportableTypeEx) {
                        // type from current module no need to import
                        if (currentModule == type.module() || type.isBuiltin()) {
                            return true
                        }

                        typesToImport.add(type)
                    }

                    return true
                }
            })
        }

        return typesToImport
    }

    fun getMethodList(project: Project, type: VlangTypeEx): List<VlangMethodDeclaration> {
        return CachedValuesManager.getManager(project).getCachedValue(type) {
            CachedValueProvider.Result.create(
                calcMethods(project, type), PsiModificationTracker.MODIFICATION_COUNT
            )
        }
    }

    private fun calcMethods(project: Project, type: VlangTypeEx): List<VlangMethodDeclaration> {
        val moduleName = type.module()
        val typeName = getTypeName(type)
        if (moduleName.isEmpty() || typeName.isEmpty()) return emptyList()
        val key = "$moduleName.$typeName"
        val declarations = VlangMethodIndex.find(key, project, null, null)
        if (declarations.size == 1 || type !is VlangResolvableTypeEx<*>) {
            return declarations.toList()
        }

        val resolved = type.resolve(project) ?: return declarations.toList()
        val containingFile = resolved.containingFile as? VlangFile ?: return declarations.toList()
        val isTestFile = containingFile.isTestFile()
        val fromTests = containingFile.fromTests()
        val fromModules = containingFile.fromModules()

        var filtered = declarations.toList()
        if (!isTestFile) {
            filtered = filtered.filter {
                val variantContainingFile = it.containingFile
                variantContainingFile is VlangFile && !variantContainingFile.isTestFile()
            }
        }
        if (!fromTests) {
            filtered = filtered.filter {
                val variantContainingFile = it.containingFile
                variantContainingFile is VlangFile && !variantContainingFile.fromTests()
            }
        }
        if (!fromModules) {
            filtered = filtered.filter {
                val variantContainingFile = it.containingFile
                variantContainingFile is VlangFile && !variantContainingFile.fromModules()
            }
        }

        return filtered
    }

    private fun getTypeName(o: VlangTypeEx): String {
        if (o is VlangSumTypeEx) {
            return o.name
        }

        return o.toString()
    }

    fun getDefaultValue(element: PsiElement, type: VlangTypeEx?): String = when (type) {
        is VlangPrimitiveTypeEx        -> {
            when (type.name) {
                VlangPrimitiveTypes.BOOL   -> "false"
                VlangPrimitiveTypes.RUNE   -> "`0`"
                VlangPrimitiveTypes.CHAR   -> "0"
                VlangPrimitiveTypes.STRING -> "''"
                VlangPrimitiveTypes.NIL    -> "unsafe { nil }"

                VlangPrimitiveTypes.BYTE,
                VlangPrimitiveTypes.INT,
                VlangPrimitiveTypes.I8,
                VlangPrimitiveTypes.I16,
                VlangPrimitiveTypes.I32,
                VlangPrimitiveTypes.I64,
                VlangPrimitiveTypes.ISIZE,
                VlangPrimitiveTypes.USIZE,
                VlangPrimitiveTypes.U8,
                VlangPrimitiveTypes.U16,
                VlangPrimitiveTypes.U32,
                VlangPrimitiveTypes.U64,
                                           -> "0"

                VlangPrimitiveTypes.F32,
                VlangPrimitiveTypes.F64,
                                           -> "0.0"

                VlangPrimitiveTypes.VOIDPTR,
                VlangPrimitiveTypes.BYTEPTR,
                VlangPrimitiveTypes.CHARPTR,
                                           -> "unsafe { nil }"
            }
        }

        is VlangStringTypeEx           -> "''"
        is VlangFixedSizeArrayTypeEx   -> "[]!"
        is VlangArrayTypeEx            -> "[]"
        is VlangMapTypeEx              -> "{}"
        is VlangChannelTypeEx          -> "chan ${type.inner}{}"
        is VlangFunctionTypeEx         -> "fn ${type.signature.text} {}"
        is VlangAliasTypeEx            -> getDefaultValue(element, type.inner)
        is VlangSumTypeEx              -> getDefaultValue(element, type.types.first())
        is VlangGenericInstantiationEx -> getDefaultValue(element, type.inner)
        is VlangInterfaceTypeEx        -> "unsafe { nil }"
        is VlangUnionTypeEx            -> "unsafe { nil }"
        is VlangStructTypeEx           -> type.readableName(element) + "{}"
        is VlangPointerTypeEx          -> "unsafe { nil }"
        is VlangVoidPtrTypeEx          -> "unsafe { nil }"
        is VlangThreadTypeEx           -> "unsafe { nil }"
        is VlangNoneTypeEx             -> "none"
        is VlangAnyTypeEx              -> "0"
        else                           -> "0"
    }
}
