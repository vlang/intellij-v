package io.vlang.projectWizard.flavors

import com.intellij.openapi.util.SystemInfo
import io.vlang.projectWizard.VlangToolchainFlavor
import io.vlang.utils.toPath
import java.nio.file.Path
import kotlin.io.path.isDirectory

class VlangUnixToolchainFlavor : VlangToolchainFlavor() {
    override fun getHomePathCandidates(): Sequence<Path> {
        return sequenceOf("/usr/local", "/usr")
            .map { it.toPath() }
            .filter { it.isDirectory() }
    }

    override fun isApplicable(): Boolean = SystemInfo.isUnix
}
