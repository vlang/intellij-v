package io.vlang.projectWizard.flavors

import io.vlang.projectWizard.VlangToolchainFlavor
import io.vlang.utils.toPath
import java.nio.file.Path
import kotlin.io.path.isDirectory

class VlangUserHomeToolchainFlavor : VlangToolchainFlavor() {
    override fun getHomePathCandidates(): Sequence<Path> {
        val home = System.getProperty("user.home").toPath()
        return sequenceOf(home.resolve("v"), home.resolve("vlang"))
            .filter { it.isDirectory() }
    }
}
