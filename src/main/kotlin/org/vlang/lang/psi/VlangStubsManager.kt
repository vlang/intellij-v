package org.vlang.lang.psi

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import org.vlang.configurations.VlangConfiguration
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.lang.psi.types.VlangStructTypeEx
import org.vlang.lang.psi.types.VlangTypeEx

class VlangStubsManager(private val project: Project) {
    private val resolvedFiles: MutableMap<String, VlangFile> = LinkedHashMap(20)
    private val resolvedTypes: MutableMap<String, VlangTypeEx> = LinkedHashMap(20)
    private val isUnitTest = ApplicationManager.getApplication().isUnitTestMode

    fun findFile(name: String): VlangFile? {
        if (resolvedFiles.containsKey(name) && !isUnitTest) {
            return resolvedFiles[name]
        }
        val stubDir = VlangConfiguration.getInstance(project).stubsLocation

        val parts = name.split("/")
        var virtualFile = stubDir
        for (part in parts) {
            virtualFile = virtualFile?.findChild(part) ?: return null
        }

        if (virtualFile == null) {
            return null
        }

        val file = PsiManager.getInstance(project).findFile(virtualFile) as? VlangFile ?: return null
        resolvedFiles[name] = file
        return file
    }

    fun findStructType(fileName: String, name: String): VlangStructTypeEx? {
        if (resolvedTypes.containsKey(name) && !isUnitTest) {
            return resolvedTypes[name] as? VlangStructTypeEx
        }
        val psiFile = findFile(fileName) ?: return null
        val struct = psiFile.getStructs()
            .firstOrNull { it.name == name } ?: return null
        val type = struct.structType.toEx() as? VlangStructTypeEx ?: return null
        resolvedTypes[name] = type
        return type
    }
}
