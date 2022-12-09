package org.vlang.configurations

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import org.vlang.configurations.VlangProjectSettingsState.Companion.projectSettings
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import java.io.File

@Service
class VlangConfiguration(private val project: Project) {
    companion object {
        fun getInstance(project: Project) = project.service<VlangConfiguration>()
    }

    private val settings
        get() = project.projectSettings

    val toolchainLocation: VirtualFile?
        get() = findFile(settings.toolchainLocation)

    val stdlibLocation: VirtualFile?
        get() = findFile(settings.customStdlibLocation ?: (settings.toolchainLocation + "/vlib"))

    val modulesLocation: VirtualFile?
        get() = findFile(settings.customModulesLocation ?: modulesLocation())

    val builtinLocation: VirtualFile?
        get() = stdlibLocation?.findChild(VlangCodeInsightUtil.BUILTIN_MODULE)

    val srcLocation: VirtualFile?
        get() = findFileInProject("src")

    val localModulesLocation: VirtualFile?
        get() = findFileInProject("modules")

    val stubsLocation: VirtualFile?
        get() = getStubs()

    private fun findFile(path: String): VirtualFile? {
        if (path.isEmpty()) return null
        return VirtualFileManager.getInstance().findFileByUrl(VfsUtilCore.pathToUrl(path))
    }

    private fun findFileInProject(path: String): VirtualFile? {
        val projectDir = project.guessProjectDir() ?: return null
        return projectDir.findFileByRelativePath(path)
    }

    private fun modulesLocation(): String {
        val vmodulesPathString = System.getenv("VMODULES") ?: return defaultModulesLocation()
        val paths = vmodulesPathString.split(File.pathSeparatorChar)
        return paths.firstOrNull() ?: defaultModulesLocation()
    }

    private fun defaultModulesLocation(): String {
        return FileUtil.expandUserHome("~/.vmodules")
    }

    private fun getStubs(): VirtualFile? {
        val url = this::class.java.classLoader.getResource("stubs")
        if (url != null) {
            val root = VfsUtil.findFileByURL(url)
            if (root != null) {
                return root
            }
        }

        return null
    }
}
