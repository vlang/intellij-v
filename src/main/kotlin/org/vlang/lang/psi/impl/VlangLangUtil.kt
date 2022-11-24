package org.vlang.lang.psi.impl

import com.intellij.openapi.project.Project
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import org.vlang.lang.psi.VlangMethodDeclaration
import org.vlang.lang.psi.types.VlangTypeEx
import org.vlang.lang.stubs.index.VlangMethodIndex

object VlangLangUtil {
    fun findMethod(project: Project, type: VlangTypeEx, name: String): VlangMethodDeclaration? {
        return getMethodList(project, type).firstOrNull { it.name == name }
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
        return o.toString()
    }
}
