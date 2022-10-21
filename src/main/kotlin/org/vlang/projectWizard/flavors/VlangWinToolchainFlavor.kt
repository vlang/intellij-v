package org.vlang.projectWizard.flavors

import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.io.FileUtil
import org.vlang.projectWizard.VlangToolchainFlavor
import org.vlang.utils.list
import org.vlang.utils.toPath
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.isDirectory

class VlangWinToolchainFlavor : VlangToolchainFlavor() {
    override fun getHomePathCandidates(): Sequence<Path> {
        val programFiles = System.getenv("ProgramFiles")?.toPath() ?: return emptySequence()
        if (!programFiles.exists() || !programFiles.isDirectory()) return emptySequence()
        return programFiles.list()
            .filter { it.isDirectory() }
            .filter {
                val name = FileUtil.getNameWithoutExtension(it.fileName.toString())
                name.lowercase() == "v" || name.lowercase().startsWith("vlang")
            }
    }

    override fun isApplicable(): Boolean = SystemInfo.isWindows
}
