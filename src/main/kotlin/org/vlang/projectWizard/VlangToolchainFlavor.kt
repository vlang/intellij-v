package org.vlang.projectWizard

import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.util.io.isDirectory
import org.vlang.projectWizard.ToolchainUtil.hasExecutable
import org.vlang.projectWizard.ToolchainUtil.pathToExecutable
import java.nio.file.Path

abstract class VlangToolchainFlavor {

    fun suggestHomePaths(): Sequence<Path> = getHomePathCandidates().filter { isValidToolchainPath(it) }

    protected abstract fun getHomePathCandidates(): Sequence<Path>

    /**
     * Flavor is added to result in [getApplicableFlavors] if this method returns true.
     * @return whether this flavor is applicable.
     */
    protected open fun isApplicable(): Boolean = true

    /**
     * Checks if the path is the name of a V toolchain of this flavor.
     *
     * @param path path to check.
     * @return true if paths points to a valid home.
     */
    protected open fun isValidToolchainPath(path: Path): Boolean {
        return path.isDirectory() && hasExecutable(path, "v") && path.resolve("vlib").isDirectory()
    }

    protected open fun hasExecutable(path: Path, toolName: String): Boolean = path.hasExecutable(toolName)

    protected open fun pathToExecutable(path: Path, toolName: String): Path = path.pathToExecutable(toolName)

    companion object {
        private val EP_NAME: ExtensionPointName<VlangToolchainFlavor> =
            ExtensionPointName.create("org.vlang.toolchainFlavor")

        fun getApplicableFlavors(): List<VlangToolchainFlavor> =
            EP_NAME.extensionList.filter { it.isApplicable() }

        fun getFlavor(path: Path): VlangToolchainFlavor? =
            getApplicableFlavors().find { flavor -> flavor.isValidToolchainPath(path) }
    }
}
