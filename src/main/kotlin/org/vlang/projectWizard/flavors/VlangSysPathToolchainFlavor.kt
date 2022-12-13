package org.vlang.projectWizard.flavors

import org.vlang.projectWizard.VlangToolchainFlavor
import org.vlang.utils.toPathOrNull
import java.io.File
import java.nio.file.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.isExecutable

class VlangSysPathToolchainFlavor : VlangToolchainFlavor() {
    override fun getHomePathCandidates(): Sequence<Path> {
        return System.getenv("PATH")
            .orEmpty()
            .split(File.pathSeparator)
            .asSequence()
            .filter { it.isNotEmpty() }
            .mapNotNull { it.toPathOrNull() }
            .filter { it.isExecutable() }
            .map { it.parent }
            .filter { it.isDirectory() }
    }
}
