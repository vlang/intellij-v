package org.vlang.lang.psi.impl

import com.intellij.openapi.project.Project
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.VlangMethodDeclaration
import org.vlang.lang.psi.VlangSignature
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.lang.psi.types.VlangImportableTypeEx
import org.vlang.lang.psi.types.VlangSumTypeEx
import org.vlang.lang.psi.types.VlangTypeEx
import org.vlang.lang.psi.types.VlangTypeVisitor
import org.vlang.lang.stubs.index.VlangMethodIndex

object VlangLangUtil {
    fun findMethod(project: Project, type: VlangTypeEx, name: String): VlangMethodDeclaration? {
        return getMethodList(project, type).firstOrNull { it.name == name }
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
        return declarations.toList()
    }

    private fun getTypeName(o: VlangTypeEx): String {
        if (o is VlangSumTypeEx) {
            return o.name
        }

        return o.toString()
    }
}
