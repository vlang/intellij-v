package org.vlang.projectWizard.flavors

import com.intellij.openapi.util.io.FileUtil
import org.vlang.projectWizard.VlangToolchainFlavor
import org.vlang.utils.toPath
import java.nio.file.Path
import kotlin.io.path.isDirectory

class VupToolchainFlavor : VlangToolchainFlavor() {
    override fun getHomePathCandidates(): Sequence<Path> {
        val path = FileUtil.expandUserHome("~/v").toPath()
        return if (path.isDirectory()) {
            sequenceOf(path)
        } else {
            emptySequence()
        }
    }
}
