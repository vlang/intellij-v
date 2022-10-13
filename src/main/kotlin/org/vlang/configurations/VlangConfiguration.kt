package org.vlang.configurations

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import org.vlang.configurations.VlangProjectSettingsState.Companion.projectSettings

@Service
class VlangConfiguration(private val project: Project) {
    companion object {
        fun getInstance(project: Project) = project.service<VlangConfiguration>()
    }

    private val settings
        get() = project.projectSettings

    val toolchainLocation: VirtualFile?
        get() = findFile(settings.toolchainLocation)

    val toolchainVersion: String
        get() = settings.toolchainVersion

    val stdlibLocation: VirtualFile?
        get() = findFile(settings.stdlibLocation)

    val modulesLocation: VirtualFile?
        get() = findFile(settings.modulesLocation)

    val builtinLocation: VirtualFile?
        get() = stdlibLocation?.findChild("builtin")

    private fun findFile(path: String): VirtualFile? {
        if (path.isEmpty()) return null
        return VirtualFileManager.getInstance().findFileByUrl(VfsUtilCore.pathToUrl(path))
    }
}