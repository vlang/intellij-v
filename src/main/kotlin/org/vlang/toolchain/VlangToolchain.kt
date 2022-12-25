package org.vlang.toolchain

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import org.vlang.configurations.VlangConfigurationUtil
import java.nio.file.Path

interface VlangToolchain {
    fun name(): String
    fun version(): String
    fun compiler(): VirtualFile?
    fun stdlibDir(): VirtualFile?
    fun rootDir(): VirtualFile?
    fun homePath(): String
    fun isValid(): Boolean

    companion object {
        fun fromPath(homePath: String): VlangToolchain {
            if (homePath == "") {
                return NULL
            }

            val virtualFileManager = VirtualFileManager.getInstance()
            val rootDir = virtualFileManager.findFileByNioPath(Path.of(homePath)) ?: return NULL
            return fromDirectory(rootDir)
        }

        private fun fromDirectory(rootDir: VirtualFile): VlangToolchain {
            val version = VlangConfigurationUtil.guessToolchainVersion(rootDir.path)
            return VlangLocalToolchain(version, rootDir)
        }

        val NULL = object : VlangToolchain {
            override fun name(): String = ""
            override fun version(): String = ""
            override fun compiler(): VirtualFile? = null
            override fun stdlibDir(): VirtualFile? = null
            override fun rootDir(): VirtualFile? = null
            override fun homePath(): String = ""
            override fun isValid(): Boolean = false
        }
    }
}
