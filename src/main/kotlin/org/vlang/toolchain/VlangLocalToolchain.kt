package org.vlang.toolchain

import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.VirtualFile
import org.vlang.configurations.VlangConfigurationUtil

class VlangLocalToolchain(
    private val version: String,
    private val rootDir: VirtualFile,
) : VlangToolchain {
    private val homePath = rootDir.path
    private val executable = rootDir.findChild(VlangConfigurationUtil.STANDARD_V_COMPILER)
    private val libDir = rootDir.findChild(VlangConfigurationUtil.STANDARD_LIB_PATH)

    override fun name(): String = version

    override fun version(): String = version

    override fun compiler(): VirtualFile? = executable

    override fun stdlibDir(): VirtualFile? = libDir

    override fun rootDir(): VirtualFile = rootDir

    override fun homePath(): String = homePath

    override fun isValid(): Boolean {
        val dir = rootDir()
        return dir.isValid && dir.isInLocalFileSystem && dir.isDirectory
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VlangLocalToolchain

        return FileUtil.comparePaths(other.homePath(), homePath()) == 0
    }

    override fun hashCode(): Int = homePath.hashCode()
}
