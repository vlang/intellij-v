package org.vlang.projectWizard.flavors

import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.io.FileUtil
import org.vlang.projectWizard.VlangToolchainFlavor
import org.vlang.utils.toPath
import java.nio.file.Path
import kotlin.io.path.isDirectory

class VlangMacToolchainFlavor : VlangToolchainFlavor() {
    override fun getHomePathCandidates(): Sequence<Path> {
        return sequenceOf("/usr/local/Cellar/v", "/usr/local/v", FileUtil.expandUserHome("~/v"))
            .map { it.toPath() }
            .filter { it.isDirectory() }
    }

    override fun isApplicable(): Boolean = SystemInfo.isMac
}
