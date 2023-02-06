package org.vlang.ide

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectPostStartupActivity
import com.intellij.openapi.util.registry.Registry
import com.jetbrains.cidr.execution.debugger.evaluation.CidrValue
import org.vlang.configurations.VlangProjectStructureState.Companion.projectStructure
import org.vlang.projectWizard.VlangToolchainFlavor
import org.vlang.toolchain.VlangKnownToolchainsState
import org.vlang.toolchain.VlangToolchain
import org.vlang.toolchain.VlangToolchainService.Companion.toolchainSettings

class VlangPostStartupActivity : ProjectPostStartupActivity {
    override suspend fun execute(project: Project) {
        val knownToolchains = VlangKnownToolchainsState.getInstance().knownToolchains
        val toolchainSettings = project.toolchainSettings

        val needFindToolchains = knownToolchains.isEmpty()
        if (needFindToolchains) {
            val toolchainCandidates = setupToolchainCandidates()
            if (toolchainCandidates.isEmpty()) {
                return
            }

            toolchainSettings.setToolchain(project, VlangToolchain.fromPath(toolchainCandidates.first()))
            return
        }

        if (toolchainSettings.isNotSet() && knownToolchains.isNotEmpty()) {
            toolchainSettings.setToolchain(project, VlangToolchain.fromPath(knownToolchains.first()))
        }

        Registry.get("cidr.debugger.value.stringEvaluator").setValue(false)
        Registry.get("cidr.debugger.lldb.statics").setValue(false)

        try {
            project.putUserData(CidrValue.DO_NOT_SHOW_ADDRESSES, true)
        } catch (e: NoClassDefFoundError) {
            // ignore
        }

        project.projectStructure.determineProjectStructure(project)
    }

    companion object {
        fun setupToolchainCandidates(): List<String> {
            val toolchainCandidates = VlangToolchainFlavor.getApplicableFlavors()
                .flatMap { it.suggestHomePaths() }
                .distinct()
                .map { it.toString() }

            if (toolchainCandidates.isEmpty()) {
                return emptyList()
            }

            VlangKnownToolchainsState.getInstance().knownToolchains = toolchainCandidates.toSet()
            return toolchainCandidates
        }
    }
}
