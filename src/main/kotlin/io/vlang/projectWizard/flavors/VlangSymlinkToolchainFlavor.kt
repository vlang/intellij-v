package io.vlang.projectWizard.flavors

import com.intellij.openapi.util.SystemInfo
import io.vlang.projectWizard.VlangToolchainFlavor
import io.vlang.utils.toPath
import java.nio.file.Path
import kotlin.io.path.isDirectory

class VlangSymlinkToolchainFlavor : VlangToolchainFlavor() {
    override fun getHomePathCandidates(): Sequence<Path> {
        val pathToCompiler = try {
            SYMLINK_PATH.toPath().toRealPath()
        } catch (e: Exception) {
            return emptySequence()
        }

        val dir = pathToCompiler.parent
        return if (dir.isDirectory()) {
            sequenceOf(dir)
        } else {
            emptySequence()
        }
    }

    /**
     * On windows path to V in PATH.
     *
     * See [io.vlang.projectWizard.flavors.VlangSysPathToolchainFlavor]
     */
    override fun isApplicable(): Boolean = SystemInfo.isUnix

    companion object {
        const val SYMLINK_PATH = "/usr/local/bin/v"
    }
}
