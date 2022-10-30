package org.vlang.lang.psi.impl

import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.VlangMethodDeclaration
import org.vlang.lang.psi.VlangType
import org.vlang.lang.stubs.index.VlangMethodIndex

object VlangLangUtil {
    fun findMethod(o: VlangType, name: String): VlangMethodDeclaration? {
        return getMethodList(o).firstOrNull { it.name == name }
    }

    fun getMethodList(o: VlangType): List<VlangMethodDeclaration> {
        return CachedValuesManager.getCachedValue(o) {
            CachedValueProvider.Result.create(
                calcMethods(o), PsiModificationTracker.MODIFICATION_COUNT
            )
        }
    }

    private fun calcMethods(o: VlangType): List<VlangMethodDeclaration> {
        val file = o.containingFile.originalFile as? VlangFile ?: return emptyList()
        val moduleName = file.getModuleQualifiedName()
        val typeName = getTypeName(o)
        if (moduleName.isEmpty() || typeName.isEmpty()) return emptyList()
        val key = "$moduleName.$typeName"
        val project = file.project
        val declarations =
            VlangMethodIndex.find(key, project, GlobalSearchScope.allScope(project), null)
        return declarations.toList()
    }

    private fun getTypeName(o: VlangType): String {
        return o.identifier?.text ?: o.text
    }
}
