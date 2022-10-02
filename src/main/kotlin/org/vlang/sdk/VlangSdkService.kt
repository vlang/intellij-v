package org.vlang.sdk

import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.project.Project
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.util.SimpleModificationTracker
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile

class VlangSdkService(private val project: Project) : SimpleModificationTracker() {
    companion object {
        val LOG = logger<VlangSdkService>()

        fun getInstance(project: Project) = project.service<VlangSdkService>()

        fun getExecutablePath(sdkHomePath: String?): String? {
            if (sdkHomePath != null) {
                return "$sdkHomePath/v"
            }
            return null
        }

        fun libraryRootToSdkPath(root: VirtualFile): String {
            return VfsUtilCore.urlToPath(root.url.removeSuffix("/vlib"))
        }

        fun isSdkLibRoot(root: VirtualFile): Boolean {
            return root.isInLocalFileSystem &&
                    root.isDirectory &&
                    VfsUtilCore.findRelativeFile("v", root) != null
        }
    }

    private val ourTestSdkVersion: String = "0.3.1"

    fun getSdkHomePath(module: Module?): String? {
        return getSdk(module)?.homePath
    }

    fun getSdk(module: Module?): Sdk? {
        if (module != null) {
            val sdk = ModuleRootManager.getInstance(module).sdk
            if (sdk != null && sdk.sdkType is VlangSdkType) {
                return sdk
            }
        }
        val sdk = ProjectRootManager.getInstance(project).projectSdk
        return if (sdk != null && sdk.sdkType is VlangSdkType) sdk else null
    }

    fun getSdkVersion(module: Module?): String? {
        return ourTestSdkVersion
    }

    fun isVlangModule(module: Module?): Boolean {
        return ModuleType.get(module!!) === VlangModuleType.getInstance()
    }
}
