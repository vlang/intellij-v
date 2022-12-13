package org.vlang.projectWizard.flavors

import com.intellij.openapi.util.SystemInfo
import org.vlang.projectWizard.VlangToolchainFlavor
import org.vlang.utils.toPath
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
     * See [org.vlang.projectWizard.flavors.VlangSysPathToolchainFlavor]
     */
    override fun isApplicable(): Boolean = SystemInfo.isUnix

    companion object {
        const val SYMLINK_PATH = "/usr/local/bin/v"
    }
}
