package org.vlang.configurations

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.toolchain.VlangToolchainService.Companion.toolchainSettings
import org.vlang.utils.toPath
import java.io.File

@Service
class VlangConfiguration(private val project: Project) {
    companion object {
        fun getInstance(project: Project) = project.service<VlangConfiguration>()
    }

    private var stubs: VirtualFile? = null

    private val toolchain
        get() = project.toolchainSettings.toolchain()

    val toolchainLocation: VirtualFile?
        get() = toolchain.rootDir()

    val stdlibLocation: VirtualFile?
        get() = if (ApplicationManager.getApplication().isUnitTestMode)
            findFileByUrl(testStdlibPath())
        else
            toolchain.stdlibDir()

    val modulesLocation: VirtualFile?
        get() = if (ApplicationManager.getApplication().isUnitTestMode)
            findFileByUrl(testModulesPath())
        else
            findFile(modulesLocation())

    val builtinLocation: VirtualFile?
        get() = stdlibLocation?.findChild(VlangCodeInsightUtil.BUILTIN_MODULE)

    val srcLocation: VirtualFile?
        get() = findFileInProject("src")

    val localModulesLocation: VirtualFile?
        get() = findFileInProject("modules")

    val stubsLocation: VirtualFile?
        get() {
            if (stubs == null) {
                stubs = getStubs()
            }
            return stubs
        }

    private fun findFileByUrl(url: String): VirtualFile? {
        if (url.isEmpty()) return null
        return VirtualFileManager.getInstance().findFileByUrl(url)
    }

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

    private fun testStdlibPath() = "src/test/resources/_vlib/vlib/".toPath().toAbsolutePath().toUri().toString()
    private fun testModulesPath() = "src/test/resources/_vmodules/".toPath().toAbsolutePath().toUri().toString()
}
