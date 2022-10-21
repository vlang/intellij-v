package org.vlang.projectWizard.flavors

import org.vlang.projectWizard.VlangToolchainFlavor
import org.vlang.utils.toPath
import java.nio.file.Path
import kotlin.io.path.isDirectory

class VlangUserHomeToolchainFlavor : VlangToolchainFlavor() {
    override fun getHomePathCandidates(): Sequence<Path> {
        val home = System.getProperty("user.home").toPath()
        return sequenceOf(home.resolve("v"), home.resolve("vlang"))
            .filter { it.isDirectory() }
    }
}
