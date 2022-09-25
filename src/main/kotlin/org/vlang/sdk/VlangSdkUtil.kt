package org.vlang.sdk

import com.intellij.execution.configurations.PathEnvironmentVariableUtil
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.PsiElement
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.util.ObjectUtils
import java.io.IOException

object VlangSdkUtil {
    fun suggestSdkDirectory(): VirtualFile? {
        if (SystemInfo.isWindows) {
            return ObjectUtils.chooseNotNull(
                LocalFileSystem.getInstance().findFileByPath("C:\\V"),
                LocalFileSystem.getInstance().findFileByPath("C:\\cygwin")
            )
        }
        if (SystemInfo.isMac || SystemInfo.isLinux) {
            val fromEnv = suggestSdkDirectoryPathFromEnv()
            if (fromEnv != null) {
                return LocalFileSystem.getInstance().findFileByPath(fromEnv)
            }
            val usrLocal = LocalFileSystem.getInstance().findFileByPath("/usr/local/v")
            if (usrLocal != null)
                return usrLocal
        }
        if (SystemInfo.isMac) {
            val macPorts = "/opt/local/lib/v"
            val homeBrew = "/usr/local/Cellar/v"
            val userHome = "~/v"
            val file = FileUtil.findFirstThatExist(macPorts, homeBrew, userHome)
            if (file != null) {
                return LocalFileSystem.getInstance().findFileByIoFile(file)
            }
        }
        return null
    }

    private fun suggestSdkDirectoryPathFromEnv(): String? {
        val fileFromPath = PathEnvironmentVariableUtil.findInPath("v")
        if (fileFromPath != null) {
            try {
                val canonicalFile = fileFromPath.canonicalFile
                val path = canonicalFile.path
                if (path.endsWith("/v")) {
                    return path.removeSuffix("/v")
                }
            } catch (_: IOException) {
            }
        }
        return null
    }

    fun getSdkDirectoriesToAttach(sdkPath: String, versionString: String): List<VirtualFile> {
        val dir = getSdkSrcDir(sdkPath, versionString) ?: return emptyList()
        return listOf(dir)
    }

    private fun getInnerSdkSrcDir(sdkService: VlangSdkService, module: Module?): VirtualFile? {
        val sdkHomePath = sdkService.getSdkHomePath(null)
        val sdkVersionString = sdkService.getSdkVersion(module)
        return if (sdkHomePath != null && sdkVersionString != null)
            getSdkSrcDir(sdkHomePath, sdkVersionString)
        else
            null
    }

    fun getSdkSrcDir(project: Project, module: Module?): VirtualFile? {
        return if (module != null) {
            CachedValuesManager.getManager(project).getCachedValue(module) {
                val sdkService = VlangSdkService.getInstance(module.project)
                CachedValueProvider.Result.create(
                    getInnerSdkSrcDir(sdkService, module),
                    sdkService
                )
            }
        } else CachedValuesManager.getManager(project).getCachedValue(project) {
            val sdkService = VlangSdkService.getInstance(project)
            CachedValueProvider.Result.create(getInnerSdkSrcDir(sdkService, null), sdkService)
        }
    }

    private fun getSdkSrcDir(sdkPath: String, sdkVersion: String): VirtualFile? {
        val srcPath = getSrcLocation(sdkVersion)
        val file = VirtualFileManager.getInstance().findFileByUrl(
            VfsUtilCore.pathToUrl(FileUtil.join(sdkPath, srcPath))
        )
        return if (file != null && file.isDirectory) file else null
    }

    private fun getSrcLocation(version: String): String {
        if (ApplicationManager.getApplication().isUnitTestMode) {
            return "vlib"
        }
        return "vlib"
    }

    fun findBuiltinDir(context: PsiElement): VirtualFile? {
        val project = context.project

        return getSdkSrcDir(project, null)?.findChild("builtin")
    }
}
